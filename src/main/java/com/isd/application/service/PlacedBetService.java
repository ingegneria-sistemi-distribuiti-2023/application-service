package com.isd.application.service;

import com.isd.application.commons.error.CustomHttpResponse;
import com.isd.application.commons.error.CustomServiceException;
import com.isd.application.converter.PlacedBetConverter;
import com.isd.application.converter.PlacedBetMatchConverter;
import com.isd.application.domain.PlacedBet;
import com.isd.application.dto.MatchGambledDTO;
import com.isd.application.dto.PlacedBetDTO;
import com.isd.application.repository.PlacedBetMatchRepository;
import com.isd.application.repository.PlacedBetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

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
            placedBetMatchRepository.save(pbmcnv.toEntity(match, saved));
        }

        return bet;
    }

    public PlacedBetDTO getByBetId(Integer betId) throws Exception {
        PlacedBet entity = placedBetRepository.findOneById(betId);

        if (entity == null){
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.NOT_FOUND, "Bet Id not founded"));
        }

        PlacedBetConverter cnv = new PlacedBetConverter();
        PlacedBetDTO dto = cnv.toDTO(entity);

        return dto;
    }

    public List<PlacedBetDTO> getAllBetsByUserId(Integer userId) throws Exception {
        List<PlacedBetDTO> dto = new LinkedList<>();
        List<PlacedBet> entity = placedBetRepository.findAllByUserId(userId);
        PlacedBetConverter cnv = new PlacedBetConverter();

        for (PlacedBet bet: entity){
            dto.add(cnv.toDTO(bet));
        }

        return dto;
    }
}
