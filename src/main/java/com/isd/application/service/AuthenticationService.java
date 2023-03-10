package com.isd.application.service;

import com.isd.application.auth.BearerTokenInterceptor;
import com.isd.application.auth.SecretKeyInterceptor;
import com.isd.application.commons.error.CustomHttpResponse;
import com.isd.application.commons.error.CustomServiceException;
import com.isd.application.dto.*;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthenticationService {
    @Autowired
    RestTemplate restTemplate;
    @Value("${auth.service.url}")
    String authServiceUrl;
    @Value("${auth.service.secret}")
    private String SECRET_AUTH;
    @Value("${game.service.secret}")
    private String SECRET_GAME;
    @Value("${session.service.secret}")
    private String SECRET_SESSION;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "getUserInfoFallback")
    @Retry(name = "retryService")
    public UserBalanceDTO getUserInfo(Integer userId, String jwt) throws Exception {
        restTemplate.getInterceptors().add(new SecretKeyInterceptor(SECRET_AUTH, SECRET_GAME, SECRET_SESSION)) ;
        restTemplate.getInterceptors().add(new BearerTokenInterceptor(jwt)) ;

        ResponseEntity<UserBalanceDTO> request = restTemplate.exchange(
                authServiceUrl + "/auth/user/" + userId, HttpMethod.GET, null,
                new ParameterizedTypeReference<UserBalanceDTO>() {});

        if (request.getStatusCode() != HttpStatus.OK) {
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed"));
        }
        return request.getBody();
    }
    /**
     * Fallback method for getUserInfo
     * This method will be called only when the service is unavailable.
     * ResourceAccessException make possible to distinguish between a service unavailable and a service error (like Expired JWT)
     */
    public AuthenticationResponse getUserInfoFallback(LoginRequest loginData, ResourceAccessException e) throws CustomServiceException {
        throw new CustomServiceException(new CustomHttpResponse(HttpStatus.SERVICE_UNAVAILABLE, "Service is unavailable"));
    }

    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "getJwtFallback")
    @Retry(name = "retryService")
    public AuthenticationResponse getJwt(LoginRequest loginData) throws Exception {
        HttpEntity<LoginRequest> req = new HttpEntity<LoginRequest>(loginData);
        restTemplate.getInterceptors().add(new SecretKeyInterceptor(SECRET_AUTH, SECRET_GAME, SECRET_SESSION)) ;

        ResponseEntity<AuthenticationResponse> response = restTemplate.exchange(
                authServiceUrl + "/auth/jwt/login", HttpMethod.POST, req,
                new ParameterizedTypeReference<AuthenticationResponse>() {});

        if (response.getStatusCode() != HttpStatus.OK) {
                throw new CustomServiceException(new CustomHttpResponse(HttpStatus.UNAUTHORIZED, "Invalid jwt"));
        }

        return response.getBody();
    }
    public AuthenticationResponse getJwtFallback(LoginRequest loginData, ResourceAccessException e) throws CustomServiceException {
        throw new CustomServiceException(new CustomHttpResponse(HttpStatus.SERVICE_UNAVAILABLE, "Service is unavailable"));
    }

    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "registerFallback")
    @Retry(name = "retryService")
    public AuthenticationResponse register(UserRegistrationDTO body) throws Exception {
        HttpEntity<UserRegistrationDTO> req = new HttpEntity<UserRegistrationDTO>(body);
        restTemplate.getInterceptors().add(new SecretKeyInterceptor(SECRET_AUTH, SECRET_GAME, SECRET_SESSION)) ;

        ResponseEntity<AuthenticationResponse> response = restTemplate.exchange(
                authServiceUrl + "/auth/jwt/register", HttpMethod.POST, req,
                new ParameterizedTypeReference<AuthenticationResponse>() {});

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed"));
        }

        return response.getBody();
    }
    public AuthenticationResponse registerFallback(UserRegistrationDTO body, ResourceAccessException e) throws CustomServiceException {
        throw new CustomServiceException(new CustomHttpResponse(HttpStatus.SERVICE_UNAVAILABLE, "Service is unavailable"));
    }

    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "validateFallback")
    @Retry(name = "retryService")
    public Boolean validate(String username, String jwt) throws Exception {

        ValidationRequest body = new ValidationRequest(username, jwt);
        restTemplate.getInterceptors().add(new SecretKeyInterceptor(SECRET_AUTH, SECRET_GAME, SECRET_SESSION)) ;

        HttpEntity<ValidationRequest> req = new HttpEntity<ValidationRequest>(body);

        ResponseEntity<Boolean> response = restTemplate.exchange(
                authServiceUrl + "/auth/jwt/validate", HttpMethod.POST, req,
                new ParameterizedTypeReference<Boolean>() {});

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.UNAUTHORIZED, "Failed"));
        }

        return response.getBody();
    }
    public Boolean validateFallback(String username, String jwt, ResourceAccessException e) throws CustomServiceException {
        throw new CustomServiceException(new CustomHttpResponse(HttpStatus.SERVICE_UNAVAILABLE, "Service is unavailable"));
    }

    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "withdrawFallback")
    @Retry(name = "retryService")
    public TransactionResponseDTO withdraw(TransactionRequestDTO req) throws Exception {
        HttpEntity<TransactionRequestDTO> request = new HttpEntity<>(req);
        restTemplate.getInterceptors().add(new SecretKeyInterceptor(SECRET_AUTH, SECRET_GAME, SECRET_SESSION)) ;

        ResponseEntity<TransactionResponseDTO> transaction = restTemplate.exchange(
                authServiceUrl + "/auth/transaction/withdraw", HttpMethod.POST, request,
                new ParameterizedTypeReference<TransactionResponseDTO>() {});
        // verify status code of request
        if (transaction.getStatusCode() != HttpStatus.OK) {
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed"));
        }

        return transaction.getBody();
    }
    public TransactionResponseDTO withdrawFallback(TransactionRequestDTO req, ResourceAccessException e) throws CustomServiceException {
        throw new CustomServiceException(new CustomHttpResponse(HttpStatus.SERVICE_UNAVAILABLE, "Service is unavailable"));
    }

    @CircuitBreaker(name = "circuitBreaker", fallbackMethod = "depositFallback")
    @Retry(name = "retryService")
    public TransactionResponseDTO deposit(TransactionRequestDTO req, String jwt) throws Exception {
        HttpEntity<TransactionRequestDTO> request = new HttpEntity<>(req);
        restTemplate.getInterceptors().add(new BearerTokenInterceptor(jwt));

        ResponseEntity<TransactionResponseDTO> transaction = restTemplate.exchange(
                authServiceUrl + "/auth/transaction/deposit", HttpMethod.POST, request,
                new ParameterizedTypeReference<TransactionResponseDTO>() {});
        // verify status code of request
        if (transaction.getStatusCode() != HttpStatus.OK) {
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.NOT_FOUND, "User Not founded"));
        }

        return transaction.getBody();
    }
    public TransactionResponseDTO depositFallback(TransactionRequestDTO req, String jwt, ResourceAccessException e) throws CustomServiceException {
        throw new CustomServiceException(new CustomHttpResponse(HttpStatus.SERVICE_UNAVAILABLE, "Service is unavailable"));
    }
}
