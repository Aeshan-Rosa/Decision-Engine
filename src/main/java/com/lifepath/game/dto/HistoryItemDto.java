package com.lifepath.game.dto;

import java.time.Instant;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryItemDto {
    private Long id;
    private Long eventId;
    private String eventTitle;
    private Long choiceId;
    private String choiceText;
    private Instant decidedAt;
}
