package com.lifepath.game.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StartGameResponse {
    private Long gameId;
    private GameStateResponse state;
}
