package com.isd.application.controller;

import com.isd.application.dto.*;
import com.isd.application.service.PlacedBetService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/gamble")
@RequiredArgsConstructor
public class GambleController {
    private final PlacedBetService placedBetService;

    @PostMapping(path="/add")
    @SecurityRequirement(name = "bearerAuth")
    public @ResponseBody ResponseEntity<UserDataDTO> create(@RequestBody AddMatchDTO body, @RequestHeader("Username") String username) throws Exception {
        return new ResponseEntity<>(placedBetService.addMatchOrCreateUserData(body), HttpStatus.OK);
    }

    @PostMapping(path="/remove")
    @SecurityRequirement(name = "bearerAuth")
    public @ResponseBody ResponseEntity<UserDataDTO> del(@NotNull @RequestBody RemoveMatchDTO body, @RequestHeader("Username") String username) throws Exception {
        return new ResponseEntity<>(placedBetService.removeMatchFromBet(body), HttpStatus.OK);
    }

    @PostMapping(path="/place-bet")
    @SecurityRequirement(name = "bearerAuth")
    public @ResponseBody ResponseEntity<PlacedBetDTO> placebet(@NotNull @RequestBody PlaceBetDTO body, @RequestHeader("Username") String username, HttpServletRequest request) throws Exception {
        return new ResponseEntity<>(placedBetService.placeBet(body, request.getHeader("Authorization")), HttpStatus.OK);
    }


}
