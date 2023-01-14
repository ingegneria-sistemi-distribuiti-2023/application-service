package com.isd.application.service;

import com.isd.application.commons.OutcomeEnum;
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
public class UserService {
    private final RestTemplate restTemplate;

    @Value("${session.service.url}")
    String sessionServiceUrl;

    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserDataDTO getCurrentUserData(Integer userId) {
        // code to call user-service to get user data
        ResponseEntity<UserDataDTO> response = restTemplate.exchange(
                sessionServiceUrl + "/api/sessions/" + userId, HttpMethod.GET, null,
                new ParameterizedTypeReference<UserDataDTO>() {});

        if (response.getStatusCode() != HttpStatus.OK) {
            return null;
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
                    throw new Exception("Match already in bet");
                }
            }
        }

        if (selectedBet == null) {
            throw new Exception("Bet id " + betId + " not found");
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

    public ResponseEntity<UserDataDTO> updateUserData(UserDataDTO userData) {
        HttpEntity<UserDataDTO> request = new HttpEntity<>(userData);

        ResponseEntity<UserDataDTO> updatedUserDataRequest = restTemplate.exchange(
                sessionServiceUrl + "/api/sessions/", HttpMethod.POST, request,
                new ParameterizedTypeReference<UserDataDTO>() {});
        // verify status code of request
        if (updatedUserDataRequest.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return updatedUserDataRequest;
    }

}
