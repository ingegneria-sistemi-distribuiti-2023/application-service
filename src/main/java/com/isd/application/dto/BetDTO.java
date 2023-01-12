package com.isd.application.dto;

import java.io.Serializable;
import java.util.List;

import com.isd.application.commons.CurrencyEnum;

public class BetDTO implements Serializable{
    private static final long serialVersionUID = 6529665465167757690L;
    private int betValue;
    private CurrencyEnum currency;
    private List<MatchGambledDTO> games;
    private long ts;
    private static int MAX_MATCH = 3;

    public BetDTO() {
    }

    public BetDTO(int betValue, CurrencyEnum currency, List<MatchGambledDTO> games, long ts) {
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

    public List<MatchGambledDTO> getGames() {
        return games;
    }

    //set games
    public void setGames(List<MatchGambledDTO> games) {
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

    // hashCode override
    @Override
    public int hashCode() {
        int result;
        long temp;
        result = betValue;
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (games != null ? games.hashCode() : 0);
        temp = Double.doubleToLongBits(ts);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public MatchGambledDTO getMatchByMatchId(Integer gameId) throws Exception{
        for (MatchGambledDTO game: getGames()){
            if (game.getGameId() == gameId){
                return game;
            }
        }
        return null;
    }

    public void addMatch(MatchGambledDTO gamble) throws Exception {
        // Check if the number of bets in the list is less than or equal to 3
        if (games.size() <= MAX_MATCH) {
            // Iterate through the list of bets
            for (MatchGambledDTO current: games){
                if (gamble.getGameId() == current.getGameId()){
                    throw new Exception("Match already in the list");
                }
            }
            games.add(gamble);
        } else {
            throw new Exception("Cannot add more than 3 bets to the list.");
        }
    }

    public void removeMatch(Integer gameId) throws Exception {

        MatchGambledDTO toRemove = null;

        for (MatchGambledDTO current: games){
            if (gameId == current.getGameId()){
                toRemove = current;
            }
        }

        if (toRemove == null){
            throw new Exception("GameId " + gameId + "not founded " );
        }

        games.remove(toRemove);

    }
}