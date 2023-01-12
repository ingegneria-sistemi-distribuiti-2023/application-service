package com.isd.application.controller;


import com.isd.application.commons.CurrencyEnum;
import com.isd.application.commons.OutcomeEnum;
import com.isd.application.dto.*;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedList;
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
    // TODO: Eliminare Session-Id dagli headers in quanto si occuperà automaticamente session-service di prendere la sessione dal database
    public @ResponseBody ResponseEntity<UserDataDTO> create(@RequestHeader(value = "Session-Id", required = false) String sessionId, @RequestBody AddMatchDTO body) throws Exception {
        ResponseEntity<UserDataDTO> toRet = null;

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

            Integer gameId = body.getGameId();
            Long betId = body.getBetId();
            OutcomeEnum outcome = body.getOutcome();

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

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Session-Id", sessionId);

            // se betId non è presente crei una nuova schedina
            if (betId == null){
                // inizializzo la scommessa
                BetDTO newBet = new BetDTO();
                newBet.setTs(System.currentTimeMillis());
                // inizializzo una nuova lista di partite
                List<MatchGambledDTO> gambledMatches = new LinkedList<>();

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

                // TODO: refactoring
                // l'utente non ha una sessione e di conseguenza va istanziata
                if (currentUserData == null){
                    currentUserData = new UserDataDTO();
                    currentUserData.setUserId(body.getUserId());
                    currentUserData.setListOfBets(new LinkedList<>());
                    currentUserData.addBet(newBet);

                    HttpEntity<UserDataDTO> request = new HttpEntity<>(currentUserData);

                    // aggiorno la sessione esistente contattando il game service
                    ResponseEntity<UserDataDTO> newUserDataRequest = restTemplate.exchange(
                            sessionServiceUrl + "/api/sessions/", HttpMethod.POST, request,
                            new ParameterizedTypeReference<UserDataDTO>() {});
                    // verify status code of request
                    if (newUserDataRequest.getStatusCode() != HttpStatus.OK) {
                        return ResponseEntity.status(newUserDataRequest.getStatusCode()).build();
                    }
                    currentUserData = newUserDataRequest.getBody();

                } else {
                    // aggiorna la sessione esistente
                    currentUserData.addBet(newBet);

                    HttpEntity<UserDataDTO> request = new HttpEntity<>(currentUserData, headers);

                    // aggiorno la sessione esistente contattando il game service
                    ResponseEntity<UserDataDTO> savedUserDataRequest = restTemplate.exchange(
                            sessionServiceUrl + "/api/sessions/", HttpMethod.POST, request,
                            new ParameterizedTypeReference<UserDataDTO>() {});
                    // verify status code of request
                    if (savedUserDataRequest.getStatusCode() != HttpStatus.OK) {
                        return ResponseEntity.status(savedUserDataRequest.getStatusCode()).build();
                    }
                    currentUserData = savedUserDataRequest.getBody();
                }

            } else {
                // aggiungi il match, se non è già presente in 'List<MatchGambledDTO> games' con relativa quota ed outcome dato l'id della schedina (betId)

                List<BetDTO> currentBets = currentUserData.getListOfBets();
                BetDTO selectedBet = null;

                for (BetDTO bet: currentBets){
                    if (bet.getTs() == betId){
                        selectedBet = bet;
                    }
                }

                if (selectedBet == null){
                    throw new Exception("Bet id " + betId + " not founded");
                }

                MatchGambledDTO newGamble = new MatchGambledDTO();
                newGamble.setGameId(gameId);

                newGamble.setGameId(gameId);

                newGamble.setQuoteAtTimeOfBet(currentMatch.getPayout(outcome));

                newGamble.setOutcome(body.getOutcome());
                newGamble.setTs(System.currentTimeMillis());

                currentUserData.removeBet(selectedBet);
                selectedBet.getGames().add(newGamble);
                currentUserData.addBet(selectedBet);

                HttpEntity<UserDataDTO> request = new HttpEntity<>(currentUserData, headers);

                ResponseEntity<UserDataDTO> updatedUserDataRequest = restTemplate.exchange(
                        sessionServiceUrl + "/api/sessions/", HttpMethod.POST, request,
                        new ParameterizedTypeReference<UserDataDTO>() {});
                // verify status code of request
                if (updatedUserDataRequest.getStatusCode() != HttpStatus.OK) {
                    return ResponseEntity.status(updatedUserDataRequest.getStatusCode()).build();
                }

                currentUserData = updatedUserDataRequest.getBody();

            }


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
