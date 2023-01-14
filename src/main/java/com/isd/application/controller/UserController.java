package com.isd.application.controller;

import com.isd.application.dto.*;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/app/user")
public class UserController {

    @Autowired
    RestTemplate restTemplate;

    @Value("${auth.service.url}")
    String authServiceUrl;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private ResponseEntity<AuthenticationResponse> getJwt(LoginRequest loginData) {
        HttpEntity<LoginRequest> req = new HttpEntity<LoginRequest>(loginData);

        ResponseEntity<AuthenticationResponse> request = restTemplate.exchange(
                authServiceUrl + "/auth/jwt/login", HttpMethod.POST, req,
                new ParameterizedTypeReference<AuthenticationResponse>() {});

        if (request.getStatusCode() != HttpStatus.OK) {
            HttpStatusCode stat = request.getStatusCode();
            LOGGER.warn(stat.toString());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return request;
    }

    private ResponseEntity<TransactionResponseDTO> depositToAuth(TransactionRequestDTO req) {
        HttpEntity<TransactionRequestDTO> request = new HttpEntity<>(req);

        ResponseEntity<TransactionResponseDTO> transaction = restTemplate.exchange(
                authServiceUrl + "/auth/transaction/deposit", HttpMethod.POST, request,
                new ParameterizedTypeReference<TransactionResponseDTO>() {});
        // verify status code of request
        if (transaction.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return transaction;
    }

    @PostMapping(path="/register")
    public @ResponseBody ResponseEntity<AuthenticationResponse> register(@NotNull @RequestBody UserRegistrationDTO body) throws Exception {
        HttpEntity<UserRegistrationDTO> req = new HttpEntity<UserRegistrationDTO>(body);

        ResponseEntity<AuthenticationResponse> request = restTemplate.exchange(
                authServiceUrl + "/auth/jwt/register", HttpMethod.POST, req,
                new ParameterizedTypeReference<AuthenticationResponse>() {});

        if (request.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return request;
    }

    @PostMapping(path="/login")
    public @ResponseBody void login(@NotNull @RequestBody LoginRequest body) throws Exception {

        ResponseEntity<AuthenticationResponse> jwt = getJwt(body);

        LOGGER.info(String.valueOf(jwt.getStatusCode()));

    }

    @PostMapping(path="/logout")
    public @ResponseBody void logout(@NotNull @RequestBody LoginRequest body) throws Exception {

    }

    @PostMapping(path="/deposit")
    public @ResponseBody ResponseEntity<TransactionResponseDTO> deposit(@NotNull @RequestBody TransactionRequestDTO body) throws Exception {
        ResponseEntity<TransactionResponseDTO> deposit = depositToAuth(body);

        return deposit;
    }

}
