package com.isd.application.service;

import com.isd.application.auth.SecretKeyInterceptor;
import com.isd.application.commons.error.CustomHttpResponse;
import com.isd.application.commons.error.CustomServiceException;
import com.isd.application.dto.UserDataDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SessionService {
    @Autowired
    RestTemplate restTemplate;
    @Value("${auth.service.secret}")
    private String SECRET_AUTH;
    @Value("${game.service.secret}")
    private String SECRET_GAME;
    @Value("${session.service.secret}")
    private String SECRET_SESSION;
    @Value("${session.service.url}")
    String sessionServiceUrl;


    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "getCurrentUserDataFallback")
    @Retry(name = "circuitBreaker")
    public UserDataDTO getCurrentUserData(Integer userId) throws Exception {
        // code to call user-service to get user data
        restTemplate.getInterceptors().add(new SecretKeyInterceptor(SECRET_AUTH, SECRET_GAME, SECRET_SESSION)) ;

        ResponseEntity<UserDataDTO> response = restTemplate.exchange(
                sessionServiceUrl + "/session/" + userId, HttpMethod.GET, null,
                new ParameterizedTypeReference<UserDataDTO>() {});

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.NOT_FOUND, "Session not founded"));
        }
        return response.getBody();
    }
    public UserDataDTO getCurrentUserDataFallback(Integer userId, Exception e) throws CustomServiceException {
        throw new CustomServiceException(new CustomHttpResponse(HttpStatus.SERVICE_UNAVAILABLE, "Service is unavailable"));
    }

    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "updateUserDataFallback")
    @Retry(name = "circuitBreaker")
    public UserDataDTO updateUserData(UserDataDTO userData) throws Exception{
        HttpEntity<UserDataDTO> request = new HttpEntity<>(userData);
        restTemplate.getInterceptors().add(new SecretKeyInterceptor(SECRET_AUTH, SECRET_GAME, SECRET_SESSION)) ;

        ResponseEntity<UserDataDTO> response = restTemplate.exchange(
                sessionServiceUrl + "/session/", HttpMethod.POST, request,
                new ParameterizedTypeReference<UserDataDTO>() {});

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"));
        }

        return response.getBody();
    }
    public UserDataDTO updateUserDataFallback(UserDataDTO userData, Exception e) throws CustomServiceException {
        throw new CustomServiceException(new CustomHttpResponse(HttpStatus.SERVICE_UNAVAILABLE, "Service is unavailable"));
    }
}
