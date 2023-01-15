package com.isd.application.domain;

import com.isd.application.commons.OutcomeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "matches")
public class PlacedBetMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placed_bet", nullable = false)
    private PlacedBet placedBet;

    @Column(name = "match_id")
    private Integer matchId;

    @Enumerated(EnumType.STRING)
    private OutcomeEnum outcome;
    private Double quote;
    private Long ts;

}