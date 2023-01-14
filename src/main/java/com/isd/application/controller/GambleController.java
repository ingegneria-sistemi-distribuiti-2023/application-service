package com.isd.application.controller;


import com.isd.application.commons.CurrencyEnum;
import com.isd.application.commons.OutcomeEnum;
import com.isd.application.commons.PlacedBetEnum;
import com.isd.application.commons.TransactionStatus;
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

    @Value("${auth.service.url}")
    String authServiceUrl;

    private ResponseEntity<UserBalanceDTO> getUserInfo(Integer userId) {
        ResponseEntity<UserBalanceDTO> request = restTemplate.exchange(
                authServiceUrl + "/auth/user/" + userId, HttpMethod.GET, null,
                new ParameterizedTypeReference<UserBalanceDTO>() {});
        if (request.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

//            handleError(request.getStatusCode(), "Error on /auth/user/" + userId);
        }
        return request;
    }

    private ResponseEntity<UserDataDTO> getCurrentUserData(Integer userId) {
        // TODO: add Access-Token to header
        ResponseEntity<UserDataDTO> userDataRequest = restTemplate.exchange(
                sessionServiceUrl + "/api/sessions/" + userId, HttpMethod.GET, null,
                new ParameterizedTypeReference<UserDataDTO>() {});
        if (userDataRequest.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return userDataRequest;
    }

    private ResponseEntity<MatchDTO> getCurrentMatch(Integer gameId) {
        ResponseEntity<MatchDTO> matchRequest = restTemplate.exchange(
                gameServiceUrl + "/match/" + gameId, HttpMethod.GET, null,
                new ParameterizedTypeReference<MatchDTO>() {});
        if (matchRequest.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return matchRequest;
    }

    private ResponseEntity<UserDataDTO> updateUserData(UserDataDTO userData) {
         HttpEntity<UserDataDTO> request = new HttpEntity<>(userData);

        ResponseEntity<UserDataDTO> updatedUserDataRequest = restTemplate.exchange(
                sessionServiceUrl + "/api/sessions/", HttpMethod.POST, request,
                new ParameterizedTypeReference<UserDataDTO>() {});
        // verify status code of request
        if (updatedUserDataRequest.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return updatedUserDataRequest;
    }

    private ResponseEntity<TransactionResponseDTO> withdraw(TransactionRequestDTO req) {
        HttpEntity<TransactionRequestDTO> request = new HttpEntity<>(req);

        ResponseEntity<TransactionResponseDTO> transaction = restTemplate.exchange(
                authServiceUrl + "/auth/transaction/withdraw", HttpMethod.POST, request,
                new ParameterizedTypeReference<TransactionResponseDTO>() {});
        // verify status code of request
        if (transaction.getStatusCode() != HttpStatus.OK) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return transaction;
    }

    @PostMapping(path="/add")
    public @ResponseBody ResponseEntity<UserDataDTO> create(@RequestBody AddMatchDTO body) throws Exception {
        ResponseEntity<UserDataDTO> toRet = null;

        try {
            // prendo la sessione attuale
            Integer userId = body.getUserId();

            UserDataDTO currentUserData = getCurrentUserData(userId).getBody();

            Integer gameId = body.getGameId();
            Long betId = body.getBetId();
            OutcomeEnum outcome = body.getOutcome();

            // chiamo il game-service per verificare che esista il match
            // ed ottengo il dato relativo al match che si vuole giocare
            MatchDTO currentMatch = getCurrentMatch(gameId).getBody();

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

                // TODO: controllare



                for (BetDTO bet: currentBets){
                    // FIXME:
                    if (bet.getTs().toString().equals(betId.toString())){
                        selectedBet = bet;
                    }
                }

                if (selectedBet == null){
                    throw new Exception("Bet id " + betId + " not founded");
                }

                if (selectedBet.getMatchByMatchId(gameId) != null){
                    throw new Exception("Match already in bet");
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

            currentUserData = updateUserData(currentUserData).getBody();

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
            UserDataDTO currentUserData = getCurrentUserData(userId).getBody();

            Integer matchId = body.getGameId();
            Long betId = body.getBetId();

            BetDTO selectedBet = currentUserData.getBetByBetId(betId);

            if (selectedBet == null){
                throw new Exception("Error on update data session");
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

            toRet = updateUserData(currentUserData).getBody();

        } catch (Error e){
            throw new Exception("Error on " + e.getMessage());
        }

        return toRet;
    }

    @PostMapping(path="/place-bet")
    public @ResponseBody PlacedBetDTO placebet(@NotNull @RequestBody PlaceBetDTO body) throws ResponseStatusException, Exception {
        PlacedBetDTO toRet = new PlacedBetDTO();
        toRet.setStatus(PlacedBetEnum.PROCESSING);
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

            if (betId == null){
                throw new Exception("Bet Id must be provided");
            }

            // TODO: Fetch ad applicationService in modo tale che ci ritorni User

            UserBalanceDTO user = getUserInfo(userId).getBody();

            if (user == null ){
                throw new Exception("User not found");
            }

            if (user.getEnabled() == false){
                throw new Exception("User not enabled");
            }

            if (user.getCashableAmount() < betValue){
                throw new Exception("Cash amount less than betValue");
            }

            UserDataDTO currentSession = getCurrentUserData(userId).getBody();

            BetDTO selectedBet = currentSession.getBetByBetId(betId);

            if (selectedBet == null){
                throw new Exception("Invalid betId");
            }

            // TODO: scalare il user balance con un'API di authentication che si occupa di decrementare il balance di un utente

            toRet = new PlacedBetDTO();
            toRet.setUserId(userId);
            toRet.setGambledMatches(selectedBet.getGames());
            toRet.setCurrency(currency);
            toRet.setAmount(betValue);

            toRet.setPayout(selectedBet.getPayout());
            toRet.setTs(System.currentTimeMillis());
            // TODO:
            toRet.setStatus(PlacedBetEnum.PAYING);

            // chiama il service per salvare il DTO (relativo converter)
            placedBetService.save(toRet);

            TransactionRequestDTO transactionReq = new TransactionRequestDTO();

            transactionReq.setCircuit("APP_SERVICE");
            // TODO: cast
            transactionReq.setAmount(Float.valueOf(betValue));
            transactionReq.setUserId(userId);

            // chiamo l'auth per effettuare la transaction
            TransactionResponseDTO transactionResp = withdraw(transactionReq).getBody();

            if (transactionResp.getStatus() != TransactionStatus.CLOSED){
                throw new Exception("Something went wrong");
            }

            toRet.setStatus(PlacedBetEnum.PLAYED);

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
