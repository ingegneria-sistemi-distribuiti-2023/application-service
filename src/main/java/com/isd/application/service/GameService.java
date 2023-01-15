package com.isd.application.service;

import com.isd.application.commons.error.CustomHttpResponse;
import com.isd.application.commons.error.CustomServiceException;
import com.isd.application.dto.MatchDTO;
import com.isd.application.dto.TeamHistoryDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GameService {
    private final RestTemplate restTemplate;
    @Value("${game.service.url}")
    String gameServiceUrl;

    public GameService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public MatchDTO getMatchDetail(Integer matchId) throws Exception {
        ResponseEntity<MatchDTO> matchRequest = restTemplate.exchange(
                gameServiceUrl + "/match/" + matchId, HttpMethod.GET, null,
                new ParameterizedTypeReference<MatchDTO>() {});
        if (matchRequest.getStatusCode() != HttpStatus.OK) {
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.NOT_FOUND, "Match not founded"));
        }
        return matchRequest.getBody();
    }

    public List<MatchDTO> getAllMatches() throws Exception {
        ResponseEntity<List<MatchDTO>> response = restTemplate.exchange(
                gameServiceUrl + "/match/", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<MatchDTO>>() {});
        if (response.getStatusCode() != HttpStatus.OK){
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something wrong happened"));
        }
        return response.getBody();
    }

    public TeamHistoryDTO getHistoryOfTeam(Integer teamId) throws Exception {
        ResponseEntity<TeamHistoryDTO> response = restTemplate.exchange(
                gameServiceUrl + "/game/team/" + teamId, HttpMethod.GET, null,
                new ParameterizedTypeReference<TeamHistoryDTO>() {});
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.NOT_FOUND, "Match not founded"));
        }
        return response.getBody();
    }
}
