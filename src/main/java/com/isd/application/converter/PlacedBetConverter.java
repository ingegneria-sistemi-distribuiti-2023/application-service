package com.isd.application.converter;

import com.isd.application.domain.PlacedBet;
import com.isd.application.domain.PlacedBetMatch;
import com.isd.application.dto.MatchGambledDTO;
import com.isd.application.dto.PlacedBetDTO;

import java.util.LinkedList;
import java.util.List;

public class PlacedBetConverter {

    public static PlacedBetDTO toDTO(PlacedBet placedBet) {
        PlacedBetDTO dto = new PlacedBetDTO();
        dto.setId(placedBet.getId());
        dto.setUserId(placedBet.getUserId());
        dto.setAmount(placedBet.getAmount());
        dto.setCurrency(placedBet.getCurrency());
        dto.setStatus(placedBet.getStatus());
        dto.setTs(placedBet.getTs());

        Double payout = 0.0;

        List<MatchGambledDTO> matchesDto = new LinkedList<>();

        PlacedBetMatchConverter cnvMatch = new PlacedBetMatchConverter();
        if (placedBet.getMatches() != null && placedBet.getMatches().size() > 0) {
            for (PlacedBetMatch match: placedBet.getMatches()){
                payout+= match.getQuote();
                matchesDto.add(cnvMatch.toDTO(match));
            }
        }

        dto.setGambledMatches(matchesDto);

        dto.setPayout(payout);
        return dto;
    }

    public static PlacedBet toEntity(PlacedBetDTO placedBetDTO) {
        PlacedBet placedBet = new PlacedBet();
        placedBet.setId(placedBetDTO.getId());
        placedBet.setUserId(placedBetDTO.getUserId());
        placedBet.setAmount(placedBetDTO.getAmount());
        placedBet.setCurrency(placedBetDTO.getCurrency());
        placedBet.setStatus(placedBetDTO.getStatus());
        placedBet.setTs(placedBetDTO.getTs());
        return placedBet;
    }
}