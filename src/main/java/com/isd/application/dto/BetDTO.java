package com.isd.application.dto;

import java.io.Serializable;
import java.util.List;

import com.isd.application.commons.CurrencyEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BetDTO implements Serializable{
    private static final long serialVersionUID = 6529665465167757690L;
    private Integer betValue;
    private CurrencyEnum currency;
    private List<MatchGambledDTO> games;
    private Long ts;
    private static int MAX_MATCH = 3;

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

    public Double getPayout(){
        Double payout = 0.0;
        for (MatchGambledDTO match: getGames()){
            payout += match.getQuoteAtTimeOfBet();
        }
        return payout;
    }
}