package com.isd.application.controller;

import com.isd.application.dto.*;
import com.isd.application.service.AuthenticationService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationService authService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @PostMapping(path="/public/register")
    public @ResponseBody ResponseEntity<AuthenticationResponse> register(@NotNull @RequestBody UserRegistrationDTO body) throws Exception {
        return new ResponseEntity<>(authService.register(body), HttpStatus.OK);
    }

    @PostMapping(path="/public/login")
    public @ResponseBody ResponseEntity<AuthenticationResponse> login(@NotNull @RequestBody LoginRequest body) throws Exception {
        return new ResponseEntity<>(authService.getJwt(body), HttpStatus.OK);
    }

    @PostMapping(path="/user/deposit")
    @SecurityRequirement(name = "bearerAuth")
    public @ResponseBody ResponseEntity<TransactionResponseDTO> deposit(@NotNull @RequestBody TransactionRequestDTO body,  @RequestHeader("Username") String username, HttpServletRequest request) throws Exception {
        return new ResponseEntity<>(authService.deposit(body, request.getHeader("Authorization")), HttpStatus.OK);
    }

}
