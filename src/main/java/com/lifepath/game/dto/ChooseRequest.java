package com.lifepath.game.dto;

import javax.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChooseRequest {
    @NotNull
    private Long choiceId;
}
