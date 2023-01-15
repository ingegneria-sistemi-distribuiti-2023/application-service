package com.isd.application.service;

import com.isd.application.commons.error.CustomHttpResponse;
import com.isd.application.commons.error.CustomServiceException;
import com.isd.application.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthenticationService {
    private final RestTemplate restTemplate;
    @Value("${auth.service.url}")
    String authServiceUrl;

    public AuthenticationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserBalanceDTO getUserInfo(Integer userId) throws Exception {
        ResponseEntity<UserBalanceDTO> request = restTemplate.exchange(
                authServiceUrl + "/auth/user/" + userId, HttpMethod.GET, null,
                new ParameterizedTypeReference<UserBalanceDTO>() {});

        if (request.getStatusCode() != HttpStatus.OK) {
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed"));
        }
        return request.getBody();
    }

    public AuthenticationResponse getJwt(LoginRequest loginData) throws Exception {
        HttpEntity<LoginRequest> req = new HttpEntity<LoginRequest>(loginData);

        ResponseEntity<AuthenticationResponse> response = restTemplate.exchange(
                authServiceUrl + "/auth/jwt/login", HttpMethod.POST, req,
                new ParameterizedTypeReference<AuthenticationResponse>() {});

        if (response.getStatusCode() != HttpStatus.OK) {
                throw new CustomServiceException(new CustomHttpResponse(HttpStatus.UNAUTHORIZED, "Invalid jwt"));
        }

        return response.getBody();
    }

    public AuthenticationResponse register(UserRegistrationDTO body) throws Exception {
        HttpEntity<UserRegistrationDTO> req = new HttpEntity<UserRegistrationDTO>(body);

        ResponseEntity<AuthenticationResponse> response = restTemplate.exchange(
                authServiceUrl + "/auth/jwt/register", HttpMethod.POST, req,
                new ParameterizedTypeReference<AuthenticationResponse>() {});

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed"));
        }

        return response.getBody();
    }

    // TODO: validate del jwt, aggiungi quando API è pronta

    public TransactionResponseDTO withdraw(TransactionRequestDTO req) throws Exception {
        HttpEntity<TransactionRequestDTO> request = new HttpEntity<>(req);

        ResponseEntity<TransactionResponseDTO> transaction = restTemplate.exchange(
                authServiceUrl + "/auth/transaction/withdraw", HttpMethod.POST, request,
                new ParameterizedTypeReference<TransactionResponseDTO>() {});
        // verify status code of request
        if (transaction.getStatusCode() != HttpStatus.OK) {
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed"));
        }

        return transaction.getBody();
    }

    public TransactionResponseDTO deposit(TransactionRequestDTO req, String token) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<TransactionRequestDTO> request = new HttpEntity<>(req, headers);

        ResponseEntity<TransactionResponseDTO> transaction = restTemplate.exchange(
                authServiceUrl + "/auth/transaction/deposit", HttpMethod.POST, request,
                new ParameterizedTypeReference<TransactionResponseDTO>() {});
        // verify status code of request
        if (transaction.getStatusCode() != HttpStatus.OK) {
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.NOT_FOUND, "User Not founded"));
        }

        return transaction.getBody();
    }
}
