package com.lifepath.game.dto;

import com.lifepath.game.model.Stage;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDto {
    private Long id;
    private String title;
    private String description;
    private Stage stage;
    private List<ChoiceDto> choices;
}
