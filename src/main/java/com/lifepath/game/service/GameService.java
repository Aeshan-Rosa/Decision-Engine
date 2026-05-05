package com.lifepath.game.service;

import com.lifepath.game.dto.*;
import com.lifepath.game.exception.BadRequestException;
import com.lifepath.game.exception.NotFoundException;
import com.lifepath.game.model.*;
import com.lifepath.game.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final PlayerStatsRepository playerStatsRepository;
    private final EventRepository eventRepository;
    private final ChoiceRepository choiceRepository;
    private final DecisionHistoryRepository decisionHistoryRepository;

    @Transactional
    public StartGameResponse startGame() {
        Game game = Game.builder()
            .status(GameStatus.ACTIVE)
            .currentStage(Stage.SCHOOL)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
        game = gameRepository.save(game);

        PlayerStats stats = PlayerStats.builder()
            .game(game)
            .money(0).intelligence(50).happiness(50).health(50)
            .reputation(40).risk(30).relationships(45).age(14)
            .build();
        playerStatsRepository.save(stats);

        Event firstEvent = pickNextEvent(game, stats)
            .orElseThrow(() -> new BadRequestException("No starting event configured"));
        game.setCurrentEventId(firstEvent.getId());
        gameRepository.save(game);

        return StartGameResponse.builder().gameId(game.getId()).state(toState(game, stats, firstEvent)).build();
    }

    public GameStateResponse getGameState(Long gameId) {
        Game game = getGame(gameId);
        PlayerStats stats = getStats(gameId);
        Event event = getCurrentEvent(game);
        return toState(game, stats, event);
    }

    @Transactional
    public GameStateResponse choose(Long gameId, Long choiceId) {
        Game game = getGame(gameId);
        if (game.getStatus() == GameStatus.FINISHED) throw new BadRequestException("Game already finished");

        PlayerStats stats = getStats(gameId);
        Event currentEvent = getCurrentEvent(game);
        Choice choice = choiceRepository.findById(choiceId)
            .orElseThrow(() -> new NotFoundException("Choice not found"));

        if (!choice.getEvent().getId().equals(currentEvent.getId())) {
            throw new BadRequestException("Choice does not belong to current event");
        }

        applyChoice(stats, choice);
        stageFromAge(stats, game);

        decisionHistoryRepository.save(DecisionHistory.builder()
            .game(game)
            .eventId(currentEvent.getId())
            .eventTitle(currentEvent.getTitle())
            .choiceId(choice.getId())
            .choiceText(choice.getText())
            .decidedAt(Instant.now())
            .build());

        Optional<Event> next = pickNextEvent(game, stats);
        if (next.isEmpty() || decisionHistoryRepository.countByGameId(gameId) >= 14) {
            game.setStatus(GameStatus.FINISHED);
            game.setCurrentStage(Stage.ENDING);
            game.setCurrentEventId(null);
            game.setUpdatedAt(Instant.now());
            playerStatsRepository.save(stats);
            gameRepository.save(game);
            return toState(game, stats, null);
        }

        game.setCurrentEventId(next.get().getId());
        game.setUpdatedAt(Instant.now());
        playerStatsRepository.save(stats);
        gameRepository.save(game);

        return toState(game, stats, next.get());
    }

    public List<HistoryItemDto> history(Long gameId) {
        getGame(gameId);
        return decisionHistoryRepository.findByGameIdOrderByDecidedAtAsc(gameId).stream()
            .map(h -> HistoryItemDto.builder()
                .id(h.getId()).eventId(h.getEventId()).eventTitle(h.getEventTitle())
                .choiceId(h.getChoiceId()).choiceText(h.getChoiceText()).decidedAt(h.getDecidedAt()).build())
            .collect(Collectors.toList());
    }

    public EndingResponse ending(Long gameId) {
        Game game = getGame(gameId);
        if (game.getStatus() != GameStatus.FINISHED) {
            throw new BadRequestException("Game is not finished yet");
        }
        PlayerStats s = getStats(gameId);
        if (s.getHealth() < 25) return EndingResponse.builder().title("Burnout Ending").theme("health")
            .description("You pushed too hard for too long. Success came, but at the cost of your wellbeing.").build();
        if (s.getIntelligence() > 85 && s.getRisk() > 60 && s.getMoney() > 120)
            return EndingResponse.builder().title("Successful Entrepreneur").theme("startup")
                .description("You built a bold venture and turned ideas into real wealth and influence.").build();
        if (s.getReputation() > 80 && s.getMoney() > 100)
            return EndingResponse.builder().title("Corporate Leader").theme("career")
                .description("You mastered the ladder and led high-impact teams with strategic precision.").build();
        if (s.getMoney() < -30)
            return EndingResponse.builder().title("Broke Dreamer").theme("money")
                .description("Your ambitions were huge, but unstable choices left you chasing recovery.").build();
        if (s.getRelationships() > 80 && s.getHappiness() > 75)
            return EndingResponse.builder().title("Balanced Happy Life").theme("balance")
                .description("You built meaningful bonds and protected your peace while growing steadily.").build();
        if (s.getReputation() > 85 && s.getRelationships() > 70)
            return EndingResponse.builder().title("Social Icon").theme("social")
                .description("People trust you, follow you, and your influence shapes every room you enter.").build();
        if (s.getIntelligence() > 90)
            return EndingResponse.builder().title("Academic Genius").theme("academic")
                .description("A lifetime of disciplined study made you a respected voice in your field.").build();

        return EndingResponse.builder().title("Balanced Happy Life").theme("default")
            .description("Your path had wins and setbacks, but you built a life with purpose and resilience.").build();
    }

    private Optional<Event> pickNextEvent(Game game, PlayerStats stats) {
        Stage stage = game.getCurrentStage();
        List<Long> seen = decisionHistoryRepository.findByGameIdOrderByDecidedAtAsc(game.getId()).stream()
            .map(DecisionHistory::getEventId).collect(Collectors.toList());

        // Events are gated by stage, age window, and stat thresholds to branch storylines.
        return eventRepository.findByStage(stage).stream()
            .filter(e -> !seen.contains(e.getId()))
            .filter(e -> e.getMinAge() <= stats.getAge() && e.getMaxAge() >= stats.getAge())
            .filter(e -> e.getMinIntelligence() == null || stats.getIntelligence() > e.getMinIntelligence())
            .filter(e -> e.getMinRisk() == null || stats.getRisk() > e.getMinRisk())
            .filter(e -> e.getMaxHealth() == null || stats.getHealth() < e.getMaxHealth())
            .min(Comparator.comparing(Event::getId));
    }

    private void stageFromAge(PlayerStats stats, Game game) {
        Stage stage = stats.getAge() <= 17 ? Stage.SCHOOL :
            stats.getAge() <= 22 ? Stage.UNIVERSITY :
            stats.getAge() <= 30 ? Stage.EARLY_CAREER : Stage.ADULT_LIFE;
        game.setCurrentStage(stage);
    }

    private void applyChoice(PlayerStats s, Choice c) {
        s.setMoney(s.getMoney() + c.getMoneyDelta());
        s.setIntelligence(clamp01(s.getIntelligence() + c.getIntelligenceDelta()));
        s.setHappiness(clamp01(s.getHappiness() + c.getHappinessDelta()));
        s.setHealth(clamp01(s.getHealth() + c.getHealthDelta()));
        s.setReputation(clamp01(s.getReputation() + c.getReputationDelta()));
        s.setRisk(clamp01(s.getRisk() + c.getRiskDelta()));
        s.setRelationships(clamp01(s.getRelationships() + c.getRelationshipsDelta()));
        s.setAge(Math.max(14, s.getAge() + c.getAgeDelta()));
    }

    private int clamp01(int value) { return Math.max(0, Math.min(100, value)); }

    private Game getGame(Long id) { return gameRepository.findById(id).orElseThrow(() -> new NotFoundException("Game not found")); }
    private PlayerStats getStats(Long gameId) { return playerStatsRepository.findById(gameId).orElseThrow(() -> new NotFoundException("Stats missing")); }
    private Event getCurrentEvent(Game game) {
        if (game.getCurrentEventId() == null) return null;
        return eventRepository.findById(game.getCurrentEventId()).orElseThrow(() -> new NotFoundException("Current event missing"));
    }

    private GameStateResponse toState(Game game, PlayerStats stats, Event event) {
        EventDto eventDto = event == null ? null : EventDto.builder()
            .id(event.getId()).title(event.getTitle()).description(event.getDescription()).stage(event.getStage())
            .choices(choiceRepository.findByEventId(event.getId()).stream().map(c -> ChoiceDto.builder()
                .id(c.getId()).text(c.getText())
                .moneyDelta(c.getMoneyDelta()).intelligenceDelta(c.getIntelligenceDelta())
                .happinessDelta(c.getHappinessDelta()).healthDelta(c.getHealthDelta())
                .reputationDelta(c.getReputationDelta()).riskDelta(c.getRiskDelta())
                .relationshipsDelta(c.getRelationshipsDelta()).ageDelta(c.getAgeDelta())
                .build()).collect(Collectors.toList()))
            .build();

        return GameStateResponse.builder().gameId(game.getId()).status(game.getStatus()).currentStage(game.getCurrentStage())
            .stats(PlayerStatsDto.builder().money(stats.getMoney()).intelligence(stats.getIntelligence()).happiness(stats.getHappiness())
                .health(stats.getHealth()).reputation(stats.getReputation()).risk(stats.getRisk()).relationships(stats.getRelationships())
                .age(stats.getAge()).currentStage(game.getCurrentStage()).build())
            .currentEvent(eventDto).build();
    }
}
