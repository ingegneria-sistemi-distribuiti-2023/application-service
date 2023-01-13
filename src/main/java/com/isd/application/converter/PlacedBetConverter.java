package com.isd.application.converter;

import com.isd.application.domain.PlacedBet;
import com.isd.application.dto.PlacedBetDTO;

public class PlacedBetConverter {

    public static PlacedBetDTO toDTO(PlacedBet placedBet) {
        PlacedBetDTO dto = new PlacedBetDTO();
        dto.setId(placedBet.getId());
        dto.setUserId(placedBet.getUserId());
        dto.setAmount(placedBet.getAmount());
        dto.setCurrency(placedBet.getCurrency());
        dto.setStatus(placedBet.getStatus());
        dto.setTs(placedBet.getTs());
        return dto;
    }

    public static PlacedBet toEntity(PlacedBetDTO placedBetDTO) {
        PlacedBet placedBet = new PlacedBet();
//        placedBet.setId(placedBetDTO.getId());
        placedBet.setUserId(placedBetDTO.getUserId());
        placedBet.setAmount(placedBetDTO.getAmount());
        placedBet.setCurrency(placedBetDTO.getCurrency());
        placedBet.setStatus(placedBetDTO.getStatus());
        placedBet.setTs(placedBetDTO.getTs());
        return placedBet;
    }
}