package com.isd.application.service;

import com.isd.application.dto.MatchDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// TODO: Prendere come esempio. Quello che è gestito nel gambleController da mettere qui
// TODO: Da realizzare per ogni microservizio chiamato da qst server
@Service
public class GameService {
    private final RestTemplate restTemplate;
    @Value("${game.service.url}")
    String gameServiceUrl;

    public GameService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public MatchDTO getCurrentMatch(Integer gameId) {
        ResponseEntity<MatchDTO> matchRequest = restTemplate.exchange(
                gameServiceUrl + "/match/" + gameId, HttpMethod.GET, null,
                new ParameterizedTypeReference<MatchDTO>() {});
        if (matchRequest.getStatusCode() != HttpStatus.OK) {
            return null;
        }
        return matchRequest.getBody();
    }
}
