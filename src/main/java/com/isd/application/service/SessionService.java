package com.isd.application.service;

import com.isd.application.auth.SecretKeyInterceptor;
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

    // TODO: CircuitBreaker
    public UserDataDTO getCurrentUserData(Integer userId) throws Exception {
        // code to call user-service to get user data
        restTemplate.getInterceptors().add(new SecretKeyInterceptor()) ;

        ResponseEntity<UserDataDTO> response = restTemplate.exchange(
                sessionServiceUrl + "/session/" + userId, HttpMethod.GET, null,
                new ParameterizedTypeReference<UserDataDTO>() {});

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.NOT_FOUND, "Session not founded"));
        }
        return response.getBody();
    }

    // TODO: CircuitBreaker
    public UserDataDTO updateUserData(UserDataDTO userData) throws Exception{
        HttpEntity<UserDataDTO> request = new HttpEntity<>(userData);
        restTemplate.getInterceptors().add(new SecretKeyInterceptor()) ;

        ResponseEntity<UserDataDTO> response = restTemplate.exchange(
                sessionServiceUrl + "/session/", HttpMethod.POST, request,
                new ParameterizedTypeReference<UserDataDTO>() {});

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"));
        }

        return response.getBody();
    }

}
