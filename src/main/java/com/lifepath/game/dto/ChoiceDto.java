package com.lifepath.game.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChoiceDto {
    private Long id;
    private String text;
    private int moneyDelta;
    private int intelligenceDelta;
    private int happinessDelta;
    private int healthDelta;
    private int reputationDelta;
    private int riskDelta;
    private int relationshipsDelta;
    private int ageDelta;
}
