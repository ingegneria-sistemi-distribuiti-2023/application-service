package com.isd.application.controller;


import com.isd.application.dto.BetDTO;
import com.isd.application.dto.MatchDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/gamble")
public class GambleController {
    // TODO: All implementations and DTOs

    @PostMapping(path="/add")
    public @ResponseBody String create(@RequestBody MatchDTO body) throws Exception {
        String toRet = null;

        try {
//            toRet = umps.createUser(body);
        } catch (Error e){
            new Exception(e.getMessage());
        }


        return toRet;
    }

    @PostMapping(path="/delete")
    public @ResponseBody String del(@NotNull @RequestParam MatchDTO match) throws Exception {
        String toRet = null;

        try {
//            toRet = umps.deleteUser(userId);
        } catch (Error e){
            new Exception(e.getMessage());
        }

        return toRet;
    }

    @PostMapping(path="/bet")
    public @ResponseBody String recharge(@NotNull @RequestBody BetDTO body) throws Exception {
        String toRet = null;

        try {
//            toRet = umps.recharge(body);
        } catch (Error e){
            new Exception(e.getMessage());
        }

        return toRet;
    }


}
