package com.isd.application.controller;

import com.isd.application.dto.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/app/user")
public class UserController {

    @Autowired
    RestTemplate restTemplate;

    @Value("${auth.service.url}")
    String userServiceUrl;

    @PostMapping(path="/create")
    public @ResponseBody void register(@NotNull @RequestBody UserRegistrationDTO body) throws Exception {

    }

    @PostMapping(path="/login")
    public @ResponseBody void login(@NotNull @RequestBody UserLoginDTO body) throws Exception {

    }

    @PostMapping(path="/logout")
    public @ResponseBody void logout(@NotNull @RequestBody UserLoginDTO body) throws Exception {

    }

    @PostMapping(path="/recharge")
    public @ResponseBody void recharge(@NotNull @RequestBody RechargeDTO body) throws Exception {

    }

}
