package com.isd.application.controller;


import com.isd.application.commons.CurrencyEnum;
import com.isd.application.dto.*;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.isd.application.commons.MatchStatus.FINISHED;
import static com.isd.application.commons.MatchStatus.PLAYING;

@RestController
@RequestMapping("/app/gamble")
public class GambleController {

    @Autowired
    RestTemplate restTemplate;

    @Value("${session.service.url}")
    String sessionServiceUrl;

    @Value("${game.service.url}")
    String gameServiceUrl;

    @PostMapping(path="/add")
    public @ResponseBody ResponseEntity<UserDataDTO> create(@RequestHeader(value = "Session-Id", required = false) String sessionId, @RequestBody AddMatchDTO body) throws Exception {
        ResponseEntity<UserDataDTO> toRet = null;
        // TODO: prendi l'API corretta
//        String sessionId = "250c52bf-8931-44e4-9b73-a347fc2ecc7f";

        try {
            // prendo la sessione attuale
            ResponseEntity<UserDataDTO> userDataRequest = restTemplate.exchange(
                    sessionServiceUrl + "/api/sessions/" + sessionId, HttpMethod.GET, null,
                    new ParameterizedTypeReference<UserDataDTO>() {});
            // verify status code of request
            if (userDataRequest.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(userDataRequest.getStatusCode()).build();
            }

            UserDataDTO currentUserData = userDataRequest.getBody();

            // if (userId == null) // TODO: gestisci creazione di una nuova sessione

            Integer gameId = body.getGameId();

            // chiamo il game-service per verificare che esista il match
            ResponseEntity<MatchDTO> matchRequest = restTemplate.exchange(
                    gameServiceUrl + "/match/" + gameId, HttpMethod.GET, null,
                    new ParameterizedTypeReference<MatchDTO>() {});

            if (matchRequest.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(matchRequest.getStatusCode()).build();
            }

            // ho il dato relativo al match che si vuole giocare
            MatchDTO currentMatch = matchRequest.getBody();

            if (currentMatch.getStatus().equals(FINISHED) ){ // || currentMatch.getStatus().equals(PLAYING)){
                throw new Exception("Too late!");
            }

            // se betId non è presente crei una nuova schedina
            if (body.getBetId() == null){
                // inizializzo la scommessa
                BetDTO newBet = new BetDTO();
                newBet.setTs(System.currentTimeMillis());
                // inizializzo una nuova lista di partite
                List<MatchGambledDTO> gambledMatches = new ArrayList<>();

                // creo la nuova "bet"

                MatchGambledDTO gamble = new MatchGambledDTO();
                gamble.setGameId(gameId);

                // TODO: inserire un controllo per scegliere la variabile da inserire
                gamble.setQuoteAtTimeOfBet(currentMatch.getAwayWinPayout());

                gamble.setOutcome(body.getOutcome());
                gamble.setTs(System.currentTimeMillis());

                gambledMatches.add(gamble);

                // FIXME:
                newBet.setBetValue(10);
                newBet.setCurrency(CurrencyEnum.EUR);
                newBet.setGames(gambledMatches);

                currentUserData.addBet(newBet);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Session-Id", sessionId);

                HttpEntity<UserDataDTO> request = new HttpEntity<>(currentUserData, headers);

                // aggiorno la sessione esistente contattando il game service
                ResponseEntity<UserDataDTO> savedUserDataRequest = restTemplate.exchange(
                        sessionServiceUrl + "/api/sessions/", HttpMethod.POST, request,
                        new ParameterizedTypeReference<UserDataDTO>() {});
                // verify status code of request
                if (savedUserDataRequest.getStatusCode() != HttpStatus.OK) {
                    return ResponseEntity.status(savedUserDataRequest.getStatusCode()).build();
                }

                // FIXME: trova sol più elegante
                currentUserData = savedUserDataRequest.getBody();

            } else {
                // aggiungi il match, se non è già presente in 'List<MatchGambledDTO> games' con relativa quota ed outcome dato l'id della schedina (betId)
                throw new Exception("Not handled yet");

            }

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

            toRet = ResponseEntity.ok(currentUserData);

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
