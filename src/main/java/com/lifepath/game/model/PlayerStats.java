package com.lifepath.game.model;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "player_stats")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlayerStats {
    @Id
    private Long gameId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "game_id")
    private Game game;

    private int money;
    private int intelligence;
    private int happiness;
    private int health;
    private int reputation;
    private int risk;
    private int relationships;
    private int age;
}
