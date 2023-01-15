package com.isd.application.service;

import com.isd.application.commons.OutcomeEnum;
import com.isd.application.commons.error.CustomHttpResponse;
import com.isd.application.commons.error.CustomServiceException;
import com.isd.application.dto.BetDTO;
import com.isd.application.dto.MatchDTO;
import com.isd.application.dto.MatchGambledDTO;
import com.isd.application.dto.UserDataDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class SessionService {
    private final RestTemplate restTemplate;

    @Value("${session.service.url}")
    String sessionServiceUrl;

    public SessionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserDataDTO getCurrentUserData(Integer userId) throws Exception {
        // code to call user-service to get user data
        ResponseEntity<UserDataDTO> response = restTemplate.exchange(
                sessionServiceUrl + "/api/sessions/" + userId, HttpMethod.GET, null,
                new ParameterizedTypeReference<UserDataDTO>() {});

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.NOT_FOUND, "Session not founded"));
        }
        return response.getBody();
    }

    public UserDataDTO addMatchToBet(UserDataDTO currentUserData, MatchDTO currentMatch, Long betId, OutcomeEnum outcome) throws Exception {
        List<BetDTO> currentBets = currentUserData.getListOfBets();
        BetDTO selectedBet = null;

        // check if the match is already in the bet
        for (BetDTO bet : currentBets) {
            if (bet.getTs().equals(betId)) {
                selectedBet = bet;
                if (selectedBet.getMatchByMatchId(currentMatch.getId()) != null) {
                    throw new CustomServiceException(new CustomHttpResponse(HttpStatus.BAD_REQUEST, "Match already added"));
                }
            }
        }

        if (selectedBet == null) {
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.BAD_REQUEST, "Bet id not founded"));
        }

        MatchGambledDTO newGamble = new MatchGambledDTO();
        newGamble.setGameId(currentMatch.getId());
        newGamble.setQuoteAtTimeOfBet(currentMatch.getPayout(outcome));
        newGamble.setOutcome(outcome);
        newGamble.setTs(System.currentTimeMillis());

        currentUserData.removeBet(selectedBet);
        selectedBet.addMatch(newGamble);
        currentUserData.addBet(selectedBet);

        return currentUserData;
    }

    public UserDataDTO updateUserData(UserDataDTO userData) throws Exception{
        HttpEntity<UserDataDTO> request = new HttpEntity<>(userData);

        ResponseEntity<UserDataDTO> response = restTemplate.exchange(
                sessionServiceUrl + "/api/sessions/", HttpMethod.POST, request,
                new ParameterizedTypeReference<UserDataDTO>() {});

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"));
        }

        return response.getBody();
    }

}
