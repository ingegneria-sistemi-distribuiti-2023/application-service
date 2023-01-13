package com.isd.application.controller;


import com.isd.application.commons.CurrencyEnum;
import com.isd.application.commons.OutcomeEnum;
import com.isd.application.commons.PlacedBetEnum;
import com.isd.application.dto.*;

import com.isd.application.service.PlacedBetService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedList;
import java.util.List;

import static com.isd.application.commons.MatchStatus.FINISHED;
import static com.isd.application.commons.MatchStatus.TO_BE_PLAYED;

@RestController
@RequestMapping("/app/gamble")
public class GambleController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PlacedBetService placedBetService;

    @Value("${session.service.url}")
    String sessionServiceUrl;

    @Value("${game.service.url}")
    String gameServiceUrl;

    private void handleError(HttpStatusCode status, String message) {
        if (message != null) {
            throw new IllegalArgumentException(status + " - " + message);
        }
        throw new IllegalArgumentException(status.toString());
    }

    private UserDataDTO getCurrentUserData(Integer userId) {
        // TODO: add Access-Token to header
        ResponseEntity<UserDataDTO> userDataRequest = restTemplate.exchange(
                sessionServiceUrl + "/api/sessions/" + userId, HttpMethod.GET, null,
                new ParameterizedTypeReference<UserDataDTO>() {});
        if (userDataRequest.getStatusCode() != HttpStatus.OK) {
            handleError(userDataRequest.getStatusCode(), "Error on /api/sessions/");
        }
        return userDataRequest.getBody();
    }

    private MatchDTO getCurrentMatch(Integer gameId) {
        ResponseEntity<MatchDTO> matchRequest = restTemplate.exchange(
                gameServiceUrl + "/match/" + gameId, HttpMethod.GET, null,
                new ParameterizedTypeReference<MatchDTO>() {});
        if (matchRequest.getStatusCode() != HttpStatus.OK) {
            handleError(matchRequest.getStatusCode(), "Error on /match");
        }
        return matchRequest.getBody();
    }

    private UserDataDTO updateUserData(UserDataDTO userData) {
         HttpEntity<UserDataDTO> request = new HttpEntity<>(userData);

        ResponseEntity<UserDataDTO> updatedUserDataRequest = restTemplate.exchange(
                sessionServiceUrl + "/api/sessions/", HttpMethod.POST, request,
                new ParameterizedTypeReference<UserDataDTO>() {});
        // verify status code of request
        if (updatedUserDataRequest.getStatusCode() != HttpStatus.OK) {
            handleError(updatedUserDataRequest.getStatusCode(), "Error on update data session");
        }

        return updatedUserDataRequest.getBody();
    }

    /**
     * Gestisce la creazione di una nuova sessione, se non presente l'eventuale aggiornamento di una schedina già presente (aggiunta squadra)
     * */
    @PostMapping(path="/add")
    public @ResponseBody ResponseEntity<UserDataDTO> create(@RequestBody AddMatchDTO body) throws Exception {
        ResponseEntity<UserDataDTO> toRet = null;

        try {
            // prendo la sessione attuale
            Integer userId = body.getUserId();

            UserDataDTO currentUserData = getCurrentUserData(userId);

            Integer gameId = body.getGameId();
            Long betId = body.getBetId();
            OutcomeEnum outcome = body.getOutcome();

            // chiamo il game-service per verificare che esista il match
            // ed ottengo il dato relativo al match che si vuole giocare
            MatchDTO currentMatch = getCurrentMatch(gameId);

            if (currentMatch.getStatus().equals(FINISHED) ){ // || currentMatch.getStatus().equals(PLAYING)){
                throw new Exception("Too late!");
            }

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
                gamble.setQuoteAtTimeOfBet(currentMatch.getAwayWinPayout());
                gamble.setOutcome(body.getOutcome());
                gamble.setTs(System.currentTimeMillis());
                gambledMatches.add(gamble);
                newBet.setBetValue(null);
                newBet.setCurrency(null);
                newBet.setGames(gambledMatches);

                // l'utente non ha una sessione e di conseguenza va istanziata
                if (currentUserData == null){
                    currentUserData = new UserDataDTO();
                    currentUserData.setUserId(body.getUserId());
                    currentUserData.setListOfBets(new LinkedList<>());

                }

                currentUserData.addBet(newBet);

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
                selectedBet.addMatch(newGamble);
                currentUserData.addBet(selectedBet);
            }

            currentUserData = updateUserData(currentUserData);

            toRet = ResponseEntity.ok(currentUserData);

        } catch (Error e){
            new Exception(e.getMessage());
        }

        return toRet;
    }

    @PostMapping(path="/remove")
    public @ResponseBody UserDataDTO del(@NotNull @RequestBody RemoveMatchDTO body) throws Exception {
        UserDataDTO toRet = null;

        try {

            Integer userId = body.getUserId();

            // prendo la sessione attuale
            UserDataDTO currentUserData = getCurrentUserData(userId);

            Integer matchId = body.getGameId();
            Long betId = body.getBetId();

            BetDTO selectedBet = currentUserData.getBetByBetId(betId);

            if (selectedBet == null){
                handleError(HttpStatus.NOT_FOUND, "Error on update data session");
            }

            MatchGambledDTO matchToRemove = selectedBet.getMatchByMatchId(matchId);

            if (matchToRemove == null){
                throw new Exception("No MatchRemove Found");
            }

            currentUserData.removeBet(selectedBet);
            selectedBet.removeMatch(matchToRemove.getGameId());
            currentUserData.addBet(selectedBet);

            if (currentUserData.getListOfBets().size() == 1 && currentUserData.getListOfBets().get(0).getGames().size() == 0){
                currentUserData.setListOfBets(new LinkedList<>());
            }

            toRet = updateUserData(currentUserData);

        } catch (Error e){
            handleError(HttpStatus.NOT_FOUND, e.getMessage());
        }

        return toRet;
    }

    @PostMapping(path="/place-bet")
    public @ResponseBody PlacedBetDTO placebet(@NotNull @RequestBody PlaceBetDTO body) throws ResponseStatusException, Exception {
        PlacedBetDTO toRet = null;
        try {

            Integer userId = body.getUserId();
            Integer betValue = body.getBetValue();
            Long betId = body.getBetId();
            CurrencyEnum currency = body.getCurrency();

            if (currency == null || betValue <= 2){
                throw new Exception("Value mismatch");
            }

            if (userId == null){
                throw new Exception("User Id not founded");
            }

            // TODO: Fetch ad applicationService in modo tale che ci ritorni User

            // UserBalanceDto user = ....
            // CHECK user != null ,enabled = true, cashAmount > betValue

            UserDataDTO currentSession = getCurrentUserData(userId);

            if (betId == null){
                throw new Exception("Bet Id must be provided");
            }

            BetDTO selectedBet = currentSession.getBetByBetId(betId);

            if (selectedBet == null){
                throw new Exception("Invalid betId");
            }

            // tutti i Check sono passati

            // TODO: scalare il user balance con un'API di authentication che si occupa di decrementare il balance di un utente

            toRet = new PlacedBetDTO();
            toRet.setUserId(userId);
            toRet.setBetId(betId);
            toRet.setGambledMatches(selectedBet.getGames());
            toRet.setCurrency(currency);
            toRet.setAmount(betValue);

            toRet.setPayout(selectedBet.getPayout());
            toRet.setTs(System.currentTimeMillis());
            // TODO:
            toRet.setStatus(PlacedBetEnum.PLAYED);

            // Chiama il PlacedBetService per salvare il DTO (relativo converter)

            placedBetService.save(toRet);

            // se va tutto bene, rimuovi dalla sessione esiste la schedina
            currentSession.removeBet(selectedBet);
            updateUserData(currentSession);

        } catch (Error e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, null, e);
        }

        return toRet;
    }


}
