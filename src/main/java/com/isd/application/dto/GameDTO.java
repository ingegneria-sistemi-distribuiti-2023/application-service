package com.isd.application.dto;

import com.isd.application.commons.OutcomeEnum;

import java.io.Serializable;

public class GameDTO implements Serializable{
    private static final long serialVersionUID = 6529685064657757690L;
    private int gameId;
    private OutcomeEnum outcome;
    private double quoteAtTimeOfBet;
    private long ts;

    public GameDTO() {
    }

    public GameDTO(int gameId, OutcomeEnum outcome, double quoteAtTimeOfBet, long ts) {
        this.gameId = gameId;
        this.outcome = outcome;
        this.quoteAtTimeOfBet = quoteAtTimeOfBet;
        this.ts = ts;
    }

    public int getGameId() {
        return gameId;
    }

    public OutcomeEnum getOutcome() {
        return outcome;
    }

    public double getQuoteAtTimeOfBet() {
        return quoteAtTimeOfBet;
    }

    public long getTs() {
        return ts;
    }

    // toString override
    @Override
    public String toString() {
        return "Game{" +
                "gameId=" + gameId +
                ", outcome='" + outcome + '\'' +
                ", quoteAtTimeOfBet=" + quoteAtTimeOfBet +
                ", ts=" + ts +
                '}';
    }

    // equals override
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameDTO gameDTO = (GameDTO) o;

        if (gameId != gameDTO.gameId) return false;
        if (Double.compare(gameDTO.quoteAtTimeOfBet, quoteAtTimeOfBet) != 0) return false;
        if (ts != gameDTO.ts) return false;
        return outcome == gameDTO.outcome;
    }
}
