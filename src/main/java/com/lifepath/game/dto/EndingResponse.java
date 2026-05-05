package com.lifepath.game.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EndingResponse {
    private String title;
    private String description;
    private String theme;
}
