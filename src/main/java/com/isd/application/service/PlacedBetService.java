package com.isd.application.service;

import com.isd.application.converter.PlacedBetConverter;
import com.isd.application.converter.PlacedBetMatchConverter;
import com.isd.application.domain.PlacedBet;
import com.isd.application.dto.MatchGambledDTO;
import com.isd.application.dto.PlacedBetDTO;
import com.isd.application.repository.PlacedBetMatchRepository;
import com.isd.application.repository.PlacedBetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlacedBetService {

    @Autowired
    PlacedBetRepository placedBetRepository;

    @Autowired
    PlacedBetMatchRepository placedBetMatchRepository;

    public PlacedBetDTO save(PlacedBetDTO bet){

        PlacedBetConverter pbcnv = new PlacedBetConverter();

        PlacedBetMatchConverter pbmcnv = new PlacedBetMatchConverter();

        PlacedBet entity = pbcnv.toEntity(bet);

        PlacedBet saved = placedBetRepository.save(entity);

        for (MatchGambledDTO match : bet.getGambledMatches()){
            placedBetMatchRepository.save(pbmcnv.toEntity(match, saved.getId()));
        }

        return bet;
    }
}
