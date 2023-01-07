package com.isd.application.controller;

import com.isd.application.dto.BetDTO;
import com.isd.application.dto.RechargeDTO;
import com.isd.application.dto.UserLoginDTO;
import com.isd.application.dto.UserRegistrationDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/user")
public class UserController {

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
