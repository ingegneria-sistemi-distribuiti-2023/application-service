package com.isd.application.domain;

import com.isd.application.commons.OutcomeEnum;
import jakarta.persistence.*;


@Entity
@Table(name = "matches")
public class PlacedBetMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "placed_bet")
    private Integer placedBetId;
//    @ManyToOne
//    @JoinColumn(name = "placed_bet", referencedColumnName = "id")
//    private PlacedBet placedBet;

    @Column(name = "match_id")
    private Integer matchId;

    @Enumerated(EnumType.STRING)
    private OutcomeEnum outcome;
    private Double quote;
    private Long ts;

    public PlacedBetMatch() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

//    public PlacedBet getPlacedBet() {
//        return placedBet;
//    }
//
//    public void setPlacedBet(PlacedBet placedBet) {
//        this.placedBet = placedBet;
//    }

    public Integer getPlacedBetId() {
        return placedBetId;
    }

    public void setPlacedBetId(Integer placedBetId) {
        this.placedBetId = placedBetId;
    }

    public Integer getMatchId() {
        return matchId;
    }

    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }

    public OutcomeEnum getOutcome() {
        return outcome;
    }

    public void setOutcome(OutcomeEnum outcome) {
        this.outcome = outcome;
    }

    public Double getQuote() {
        return quote;
    }

    public void setQuote(Double quote) {
        this.quote = quote;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }
}