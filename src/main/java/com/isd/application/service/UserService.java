package com.isd.application.service;

import com.isd.application.dto.AuthenticationResponse;
import com.isd.application.dto.LoginRequest;
import com.isd.application.dto.UserRegistrationDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {
    private final RestTemplate restTemplate;

    @Value("${auth.service.url}")
    String authServiceUrl;

    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<AuthenticationResponse> getJwt(LoginRequest loginData) {
        HttpEntity<LoginRequest> req = new HttpEntity<LoginRequest>(loginData);

        ResponseEntity<AuthenticationResponse> request = restTemplate.exchange(
                authServiceUrl + "/auth/jwt/login", HttpMethod.POST, req,
                new ParameterizedTypeReference<AuthenticationResponse>() {});

        if (request.getStatusCode() != HttpStatus.OK) {
            HttpStatusCode stat = request.getStatusCode();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return request;
    }

    public ResponseEntity<AuthenticationResponse> register(UserRegistrationDTO body) {
        HttpEntity<UserRegistrationDTO> req = new HttpEntity<UserRegistrationDTO>(body);

        ResponseEntity<AuthenticationResponse> request = restTemplate.exchange(
                authServiceUrl + "/auth/jwt/register", HttpMethod.POST, req,
                new ParameterizedTypeReference<AuthenticationResponse>() {});

        if (request.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return request;
    }


}
