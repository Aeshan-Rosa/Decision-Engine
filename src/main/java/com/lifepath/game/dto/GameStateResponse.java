package com.lifepath.game.dto;

import com.lifepath.game.model.GameStatus;
import com.lifepath.game.model.Stage;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameStateResponse {
    private Long gameId;
    private GameStatus status;
    private Stage currentStage;
    private PlayerStatsDto stats;
    private EventDto currentEvent;
}
