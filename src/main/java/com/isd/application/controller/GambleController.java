package com.isd.application.controller;


import com.isd.application.dto.*;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/app/gamble")
public class GambleController {

    @Autowired
    RestTemplate restTemplate;

    @Value("${session.service.url}")
    String sessionServiceUrl;

    private static final int MAX_BET = 3;

    @PostMapping(path="/add")
    public @ResponseBody ResponseEntity<UserDataDTO> create(@RequestBody AddMatchDTO body) throws Exception {
        ResponseEntity<UserDataDTO> toRet = null;
        // TODO: prendi l'API corretta
        String sessionId = "250c52bf-8931-44e4-9b73-a347fc2ecc7f";

        try {
            // prendo la sessione attuale
            ResponseEntity<UserDataDTO> request = restTemplate.exchange(
                    sessionServiceUrl + "/api/sessions/" + sessionId, HttpMethod.GET, null,
                    new ParameterizedTypeReference<UserDataDTO>() {});
            // verify status code of request

            if (request.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(request.getStatusCode()).build();
            }

            UserDataDTO userData = request.getBody();

            toRet = ResponseEntity.ok(request.getBody());

//            if (body.getBetId() == null){
//                // crea una nuova sessione
//
//                // controlla che le sessioni a livello dell'utente non siano più del massimo consentito
//                if (userData.getListOfBets().size() == MAX_BET){
//                    throw new Exception("You have already all bets occupied");
//                }
//
//                // istanzia una nuova schedina
//            }
//
//            BetDTO currentBet = null;
//
//            for (BetDTO bet : userData.getListOfBets()){
//                if (bet.getTs() == body.getBetId()){
//                    currentBet = bet;
//                }
//            }
//
//            if (currentBet == null){
//                throw new Exception("Bet with that Id not founded");
//            }

            // aggiungi il match all'array ed aggiorna la sessione

            // ritorna UserData aggiornata

            return ResponseEntity.ok(request.getBody());
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
