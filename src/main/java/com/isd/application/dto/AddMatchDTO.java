package com.isd.application.dto;
// this class map the json object below
/*
  "add_match": {
        "userId": 1,
        "gameId": 1,
        "quote": 173, // TODO: controllare che la quota sia uguale a quella presente nel db
        "outcome": "HOME",
        "betId": 118928932 // TODO: controllare la presenza del campo e, se null, creare una nuova schedina con questa partita.
    },
 */

import com.isd.application.commons.OutcomeEnum;

public class AddMatchDTO {
    private int userId;
    private int gameId;
    private double quote;
    private OutcomeEnum outcome;
    private Long betId;

    public AddMatchDTO() {
    }

    public AddMatchDTO(int userId, int gameId, double quote, OutcomeEnum outcome, Long betId) {
        this.userId = userId;
        this.gameId = gameId;
        this.quote = quote;
        this.outcome = outcome;
        this.betId = betId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public double getQuote() {
        return quote;
    }

    public void setQuote(double quote) {
        this.quote = quote;
    }

    public OutcomeEnum getOutcome() {
        return outcome;
    }

    public void setOutcome(OutcomeEnum outcome) {
        this.outcome = outcome;
    }

    
    public Long getBetId() {
        return betId;
    }
    
    public void setBetId(Long betId) {
        this.betId = betId;
    }

    // toString override
    @Override
    public String toString() {
        return "AddMatchDTO{" +
                "userId=" + userId +
                ", gameId=" + gameId +
                ", quote=" + quote +
                ", outcome=" + outcome +
                ", betId=" + betId +
                '}';
    }

    // equals override
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddMatchDTO that = (AddMatchDTO) o;

        if (userId != that.userId) return false;
        if (gameId != that.gameId) return false;
        if (Double.compare(that.quote, quote) != 0) return false;
        if (betId != that.betId) return false;
        return outcome == that.outcome;
    }
}
