package com.isd.application.service;

import com.isd.application.commons.error.CustomHttpResponse;
import com.isd.application.commons.error.CustomServiceException;
import com.isd.application.dto.MatchDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GameService {
    private final RestTemplate restTemplate;
    @Value("${game.service.url}")
    String gameServiceUrl;

    public GameService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public MatchDTO getCurrentMatch(Integer gameId) throws Exception {
        ResponseEntity<MatchDTO> matchRequest = restTemplate.exchange(
                gameServiceUrl + "/match/" + gameId, HttpMethod.GET, null,
                new ParameterizedTypeReference<MatchDTO>() {});
        if (matchRequest.getStatusCode() != HttpStatus.OK) {
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.NOT_FOUND, "Match not founded"));
        }
        return matchRequest.getBody();
    }
}
