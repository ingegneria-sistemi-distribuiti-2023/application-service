package com.isd.application.dto;

import com.isd.application.commons.CurrencyEnum;

import java.io.Serializable;
import java.util.List;

public class BetDTO implements Serializable{
    private static final long serialVersionUID = 6529665465167757690L;
    private int betValue;
    private CurrencyEnum currency;
    private List<GameDTO> games;
    private long ts;

    public BetDTO() {
    }

    public BetDTO(int betValue, CurrencyEnum currency, List<GameDTO> games, long ts) {
        this.betValue = betValue;
        this.currency = currency;
        this.games = games;
        this.ts = ts;
    }

    public int getBetValue() {
        return betValue;
    }

    //set bet value
    public void setBetValue(int betValue) {
        this.betValue = betValue;
    }

    public CurrencyEnum getCurrency() {
        return currency;
    }

    //set currency
    public void setCurrency(CurrencyEnum currency) {
        this.currency = currency;
    }

    public List<GameDTO> getGames() {
        return games;
    }

    //set games
    public void setGames(List<GameDTO> games) {
        this.games = games;
    }

    public long getTs() {
        return ts;
    }

    //set ts
    public void setTs(long ts) {
        this.ts = ts;
    }

    // toString override
    @Override
    public String toString() {
        return "Bet{" +
                "betValue=" + betValue +
                ", currency='" + currency + '\'' +
                ", games=" + games +
                ", ts=" + ts +
                '}';
    }

    // equals override
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BetDTO betDTO = (BetDTO) o;

        if (betValue != betDTO.betValue) return false;
        if (ts != betDTO.ts) return false;
        if (currency != null ? !currency.equals(betDTO.currency) : betDTO.currency != null) return false;
        return games != null ? games.equals(betDTO.games) : betDTO.games == null;
    }
}