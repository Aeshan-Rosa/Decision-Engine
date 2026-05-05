package com.lifepath.game.model;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "choices")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Choice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private int moneyDelta;
    @Column(nullable = false)
    private int intelligenceDelta;
    @Column(nullable = false)
    private int happinessDelta;
    @Column(nullable = false)
    private int healthDelta;
    @Column(nullable = false)
    private int reputationDelta;
    @Column(nullable = false)
    private int riskDelta;
    @Column(nullable = false)
    private int relationshipsDelta;
    @Column(nullable = false)
    private int ageDelta;
}
