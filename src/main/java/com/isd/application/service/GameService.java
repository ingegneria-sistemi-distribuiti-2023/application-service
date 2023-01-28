package com.isd.application.service;

import com.isd.application.auth.SecretKeyInterceptor;
import com.isd.application.commons.error.CustomHttpResponse;
import com.isd.application.commons.error.CustomServiceException;
import com.isd.application.dto.MatchDTO;
import com.isd.application.dto.TeamHistoryDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    RestTemplate restTemplate;
    @Value("${game.service.url}")
    String gameServiceUrl;
    @Value("${auth.service.secret}")
    private String SECRET_AUTH;
    @Value("${game.service.secret}")
    private String SECRET_GAME;
    @Value("${session.service.secret}")
    private String SECRET_SESSION;

    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "getMatchDetailFallback")
    @Retry(name = "retryService")
    public MatchDTO getMatchDetail(Integer matchId) throws Exception {
        restTemplate.getInterceptors().add(new SecretKeyInterceptor(SECRET_AUTH, SECRET_GAME, SECRET_SESSION)) ;

        ResponseEntity<MatchDTO> matchRequest = restTemplate.exchange(
                gameServiceUrl + "/game/match/" + matchId, HttpMethod.GET, null,
                new ParameterizedTypeReference<MatchDTO>() {});
        if (matchRequest.getStatusCode() != HttpStatus.OK) {
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.NOT_FOUND, "Match not founded"));
        }
        return matchRequest.getBody();
    }

    public MatchDTO getMatchDetailFallback(Integer matchId, Exception e) throws CustomServiceException {
        throw new CustomServiceException(new CustomHttpResponse(HttpStatus.SERVICE_UNAVAILABLE, "Service is unavailable"));
    }

    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "getAllMatchesFallback")
    @Retry(name = "retryService")
    public List<MatchDTO> getAllMatches() throws Exception {
        restTemplate.getInterceptors().add(new SecretKeyInterceptor(SECRET_AUTH, SECRET_GAME, SECRET_SESSION)) ;

        ResponseEntity<List<MatchDTO>> response = restTemplate.exchange(
                gameServiceUrl + "/game/match/", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<MatchDTO>>() {});
        if (response.getStatusCode() != HttpStatus.OK){
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something wrong happened"));
        }
        return response.getBody();
    }

    public List<MatchDTO> getAllMatchesFallback(Exception e) throws CustomServiceException {
        throw new CustomServiceException(new CustomHttpResponse(HttpStatus.SERVICE_UNAVAILABLE, "Service is unavailable"));
    }

    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "getHistoryOfTeamFallback")
    @Retry(name = "retryService")
    public TeamHistoryDTO getHistoryOfTeam(Integer teamId) throws Exception {
        restTemplate.getInterceptors().add(new SecretKeyInterceptor(SECRET_AUTH, SECRET_GAME, SECRET_SESSION)) ;
        ResponseEntity<TeamHistoryDTO> response = restTemplate.exchange(
                gameServiceUrl + "/game/team/" + teamId, HttpMethod.GET, null,
                new ParameterizedTypeReference<TeamHistoryDTO>() {});
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.NOT_FOUND, "Match not founded"));
        }
        return response.getBody();
    }

    public TeamHistoryDTO getHistoryOfTeamFallback(Integer teamId, Exception e) throws CustomServiceException {
        throw new CustomServiceException(new CustomHttpResponse(HttpStatus.SERVICE_UNAVAILABLE, "Service is unavailable"));
    }
}
