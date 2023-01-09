package com.isd.application.controller;


import com.isd.application.dto.AddMatchDTO;
import com.isd.application.dto.PlaceBetDTO;
import com.isd.application.dto.RemoveMatchDTO;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/gamble")
public class GambleController {
    // TODO: All implementations and DTOs

    @PostMapping(path="/add")
    public @ResponseBody String create(@RequestBody AddMatchDTO body) throws Exception {
        String toRet = null;

        try {
//            toRet = umps.createUser(body);
        } catch (Error e){
            new Exception(e.getMessage());
        }


        return toRet;
    }

    @PostMapping(path="/remove")
    public @ResponseBody String del(@NotNull @RequestParam RemoveMatchDTO match) throws Exception {
        String toRet = null;

        try {
//            toRet = umps.deleteUser(userId);
        } catch (Error e){
            new Exception(e.getMessage());
        }

        return toRet;
    }

    @PostMapping(path="/place-bet")
    public @ResponseBody String recharge(@NotNull @RequestBody PlaceBetDTO body) throws Exception {
        String toRet = null;

        try {
//            toRet = umps.recharge(body);
        } catch (Error e){
            new Exception(e.getMessage());
        }

        return toRet;
    }


}
