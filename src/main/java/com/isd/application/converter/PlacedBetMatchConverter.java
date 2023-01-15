package com.isd.application.converter;

import com.isd.application.domain.PlacedBet;
import com.isd.application.domain.PlacedBetMatch;
import com.isd.application.dto.MatchGambledDTO;
import com.isd.application.repository.PlacedBetRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class PlacedBetMatchConverter {

    @Autowired
    static PlacedBetRepository rep;

    public static PlacedBetMatch toEntity(MatchGambledDTO dto, PlacedBet placedBet) {
        PlacedBetMatch gambledMatch = new PlacedBetMatch();

        gambledMatch.setMatchId(dto.getGameId());
        gambledMatch.setOutcome(dto.getOutcome());
        gambledMatch.setPlacedBet(placedBet);
        gambledMatch.setTs(dto.getTs());
        gambledMatch.setQuote(dto.getQuoteAtTimeOfBet());

        return gambledMatch;
    }

    public static MatchGambledDTO toDTO(PlacedBetMatch entity) {
        MatchGambledDTO dto = new MatchGambledDTO();

        dto.setTs(entity.getTs());
        dto.setOutcome(entity.getOutcome());
        dto.setGameId(entity.getId());
        dto.setQuoteAtTimeOfBet(entity.getQuote());

        return dto;
    }

}
