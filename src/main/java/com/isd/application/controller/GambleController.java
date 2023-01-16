package com.isd.application.controller;

import com.isd.application.dto.*;
import com.isd.application.service.PlacedBetService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/gamble")
public class GambleController {
    private final PlacedBetService placedBetService;

    public GambleController(PlacedBetService placedBetService) {
        this.placedBetService = placedBetService;
    }

    @PostMapping(path="/add")
    public @ResponseBody ResponseEntity<UserDataDTO> create(@RequestBody AddMatchDTO body) throws Exception {
        return new ResponseEntity<>(placedBetService.addMatchOrCreateUserData(body), HttpStatus.OK);
    }

    @PostMapping(path="/remove")
    public @ResponseBody ResponseEntity<UserDataDTO> del(@NotNull @RequestBody RemoveMatchDTO body) throws Exception {
        return new ResponseEntity<>(placedBetService.removeMatchFromBet(body), HttpStatus.OK);
    }

    @PostMapping(path="/place-bet")
    public @ResponseBody ResponseEntity<PlacedBetDTO> placebet(@NotNull @RequestBody PlaceBetDTO body, @RequestHeader("Authorization") String bearerToken) throws Exception {
        return new ResponseEntity<>(placedBetService.placeBet(body, bearerToken), HttpStatus.OK);
    }


}
