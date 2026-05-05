package com.lifepath.game.controller;

import com.lifepath.game.dto.*;
import com.lifepath.game.service.GameService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GameController {
    private final GameService gameService;

    @PostMapping("/start")
    public StartGameResponse start() { return gameService.startGame(); }

    @GetMapping("/{gameId}")
    public GameStateResponse get(@PathVariable Long gameId) { return gameService.getGameState(gameId); }

    @PostMapping("/{gameId}/choose")
    public GameStateResponse choose(@PathVariable Long gameId, @Valid @RequestBody ChooseRequest request) {
        return gameService.choose(gameId, request.getChoiceId());
    }

    @GetMapping("/{gameId}/history")
    public List<HistoryItemDto> history(@PathVariable Long gameId) { return gameService.history(gameId); }

    @GetMapping("/{gameId}/ending")
    public EndingResponse ending(@PathVariable Long gameId) { return gameService.ending(gameId); }
}
