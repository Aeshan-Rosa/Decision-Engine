package com.lifepath.game.model;

import javax.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "decision_history")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DecisionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(nullable = false)
    private Long eventId;

    @Column(nullable = false)
    private String eventTitle;

    @Column(nullable = false)
    private Long choiceId;

    @Column(nullable = false)
    private String choiceText;

    @Column(nullable = false)
    private Instant decidedAt;
}
