package com.lifepath.game.dto;

import com.lifepath.game.model.Stage;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerStatsDto {
    private int money;
    private int intelligence;
    private int happiness;
    private int health;
    private int reputation;
    private int risk;
    private int relationships;
    private int age;
    private Stage currentStage;
}
