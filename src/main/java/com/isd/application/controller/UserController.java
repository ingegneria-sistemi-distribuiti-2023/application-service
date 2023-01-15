package com.isd.application.controller;

import com.isd.application.dto.*;
import com.isd.application.service.TransactionService;
import com.isd.application.service.UserService;
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

    @Autowired
    UserService userService;

    @Autowired
    TransactionService transactionService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @PostMapping(path="/register")
    public @ResponseBody ResponseEntity<AuthenticationResponse> register(@NotNull @RequestBody UserRegistrationDTO body) throws Exception {
        return userService.register(body);
    }

    @PostMapping(path="/login")
    public @ResponseBody ResponseEntity<AuthenticationResponse> login(@NotNull @RequestBody LoginRequest body) throws Exception {
        return userService.getJwt(body);

    }

    @PostMapping(path="/deposit")
    public @ResponseBody ResponseEntity<TransactionResponseDTO> deposit(@NotNull @RequestBody TransactionRequestDTO body, @RequestHeader("Authorization") String bearerToken) throws Exception {
        return new ResponseEntity<>(transactionService.depositToAuth(body, bearerToken.substring("Bearer ".length())), HttpStatus.OK);
    }

}
