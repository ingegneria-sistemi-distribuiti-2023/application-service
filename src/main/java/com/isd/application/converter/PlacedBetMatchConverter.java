package com.isd.application.converter;

import com.isd.application.domain.PlacedBetMatch;
import com.isd.application.dto.MatchGambledDTO;

public class PlacedBetMatchConverter {

    // Necessario per salvare a database le informazioni del match associate ad una bettata
    public static PlacedBetMatch toEntity(MatchGambledDTO dto, Integer placedBetId) {
        PlacedBetMatch gambledMatch = new PlacedBetMatch();

        gambledMatch.setMatchId(dto.getGameId());
        gambledMatch.setOutcome(dto.getOutcome());
        gambledMatch.setPlacedBetId(placedBetId);
        gambledMatch.setTs(dto.getTs());
        gambledMatch.setQuote(dto.getQuoteAtTimeOfBet());

        return gambledMatch;
    }

}
