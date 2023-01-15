package com.isd.application.service;

import com.isd.application.commons.CurrencyEnum;
import com.isd.application.commons.OutcomeEnum;
import com.isd.application.commons.PlacedBetEnum;
import com.isd.application.commons.TransactionStatus;
import com.isd.application.commons.error.CustomHttpResponse;
import com.isd.application.commons.error.CustomServiceException;
import com.isd.application.converter.PlacedBetConverter;
import com.isd.application.converter.PlacedBetMatchConverter;
import com.isd.application.domain.PlacedBet;
import com.isd.application.dto.*;
import com.isd.application.repository.PlacedBetMatchRepository;
import com.isd.application.repository.PlacedBetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

import static com.isd.application.commons.MatchStatus.FINISHED;

@Service
public class PlacedBetService {

    private final AuthenticationService authenticationService;
    private final GameService gameService;
    private final SessionService sessionService;
    PlacedBetRepository placedBetRepository;
    PlacedBetMatchRepository placedBetMatchRepository;

    public PlacedBetService(AuthenticationService authenticationService, GameService gameService, SessionService sessionService, PlacedBetRepository placedBetRepository, PlacedBetMatchRepository placedBetMatchRepository) {
        this.authenticationService = authenticationService;
        this.gameService = gameService;
        this.sessionService = sessionService;
        this.placedBetRepository = placedBetRepository;
        this.placedBetMatchRepository = placedBetMatchRepository;
    }

    public PlacedBetDTO save(PlacedBetDTO bet){

        PlacedBetConverter pbcnv = new PlacedBetConverter();

        PlacedBetMatchConverter pbmcnv = new PlacedBetMatchConverter();

        PlacedBet entity = pbcnv.toEntity(bet);

        PlacedBet saved = placedBetRepository.save(entity);

        for (MatchGambledDTO match : bet.getGambledMatches()){
            placedBetMatchRepository.save(pbmcnv.toEntity(match, saved));
        }

        return bet;
    }

    public PlacedBetDTO getByBetId(Integer betId) throws Exception {
        PlacedBet entity = placedBetRepository.findOneById(betId);

        if (entity == null){
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.NOT_FOUND, "Bet Id not founded"));
        }

        PlacedBetConverter cnv = new PlacedBetConverter();
        PlacedBetDTO dto = cnv.toDTO(entity);

        return dto;
    }

    public List<PlacedBetDTO> getAllBetsByUserId(Integer userId) throws Exception {
        List<PlacedBetDTO> dto = new LinkedList<>();
        List<PlacedBet> entity = placedBetRepository.findAllByUserId(userId);
        PlacedBetConverter cnv = new PlacedBetConverter();

        for (PlacedBet bet: entity){
            dto.add(cnv.toDTO(bet));
        }

        return dto;
    }

    public PlacedBetDTO placeBet(PlaceBetDTO dto) throws Exception {
        PlacedBetDTO toRet = new PlacedBetDTO();
        toRet.setStatus(PlacedBetEnum.PROCESSING);

        Integer userId = dto.getUserId();
        Integer betValue = dto.getBetValue();
        Long betId = dto.getBetId();
        CurrencyEnum currency = dto.getCurrency();

        if (currency == null || betValue <= 2){
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.BAD_REQUEST, "Invalid betValue or currency"));
        }

        if (userId == null){
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.BAD_REQUEST, "Invalid user Id"));
        }

        if (betId == null){
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.BAD_REQUEST, "Invalid bet Id"));
        }

        UserBalanceDTO user = authenticationService.getUserInfo(userId);

        if (user.getCashableAmount() < betValue){
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.BAD_REQUEST, "Cash amount less than betValue"));
        }

        UserDataDTO currentSession = sessionService.getCurrentUserData(userId);

        BetDTO selectedBet = currentSession.getBetByBetId(betId);

        if (selectedBet == null){
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.BAD_REQUEST, "Invalid bet Id"));
        }

        toRet = new PlacedBetDTO();
        toRet.setUserId(userId);
        toRet.setGambledMatches(selectedBet.getGames());
        toRet.setCurrency(currency);
        toRet.setAmount(betValue);

        toRet.setPayout(selectedBet.getPayout());
        toRet.setTs(System.currentTimeMillis());
        toRet.setStatus(PlacedBetEnum.PAYING);

        // chiama il service per salvare il DTO (relativo converter)
        this.save(toRet);

        TransactionRequestDTO transactionReq = new TransactionRequestDTO();

        transactionReq.setCircuit("APP_SERVICE");
        // TODO: cast
        transactionReq.setAmount(Float.valueOf(betValue));
        transactionReq.setUserId(userId);

        // chiamo l'auth per effettuare la transaction
        TransactionResponseDTO transactionResp = authenticationService.withdraw(transactionReq);

        if (transactionResp.getStatus() != TransactionStatus.CLOSED){
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.BAD_REQUEST, "Something went wrong"));
        }

        toRet.setStatus(PlacedBetEnum.PLAYED);

        this.save(toRet);

        // se va tutto bene, rimuovi dalla sessione esiste la schedina
        currentSession.removeBet(selectedBet);
        sessionService.updateUserData(currentSession);

        return toRet;
    }

    public UserDataDTO addMatchOrCreateUserData(AddMatchDTO dto) throws Exception {

        Integer userId = dto.getUserId();

        UserDataDTO currentUserData = sessionService.getCurrentUserData(userId);

        Integer gameId = dto.getGameId();
        Long betId = dto.getBetId();
        OutcomeEnum outcome = dto.getOutcome();

        // chiamo il game-service per verificare che esista il match
        // ed ottengo il dato relativo al match che si vuole giocare
        MatchDTO currentMatch = gameService.getMatchDetail(gameId);

        if (currentMatch.getStatus().equals(FINISHED) ){
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.BAD_REQUEST, "Match is already playing"));
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
            gamble.setOutcome(dto.getOutcome());
            gamble.setTs(System.currentTimeMillis());
            gambledMatches.add(gamble);
            newBet.setBetValue(null);
            newBet.setCurrency(null);
            newBet.setGames(gambledMatches);

            // l'utente non ha una sessione e di conseguenza va istanziata
            if (currentUserData == null){
                currentUserData = new UserDataDTO();
                currentUserData.setUserId(dto.getUserId());
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
                throw new CustomServiceException(new CustomHttpResponse(HttpStatus.BAD_REQUEST, "Bet Id not founded"));
            }

            if (selectedBet.getMatchByMatchId(gameId) != null){
                throw new CustomServiceException(new CustomHttpResponse(HttpStatus.BAD_REQUEST, "Match already in bet"));
            }

            MatchGambledDTO newGamble = new MatchGambledDTO();
            newGamble.setGameId(gameId);

            newGamble.setGameId(gameId);

            newGamble.setQuoteAtTimeOfBet(currentMatch.getPayout(outcome));

            newGamble.setOutcome(dto.getOutcome());
            newGamble.setTs(System.currentTimeMillis());

            currentUserData.removeBet(selectedBet);
            selectedBet.addMatch(newGamble);
            currentUserData.addBet(selectedBet);
        }

        currentUserData = sessionService.updateUserData(currentUserData);

        return currentUserData;
    }

    public UserDataDTO removeMatchFromBet(RemoveMatchDTO dto) throws Exception {
        Integer userId = dto.getUserId();
        Integer matchId = dto.getGameId();
        Long betId = dto.getBetId();

        // prendo la sessione attuale
        UserDataDTO currentUserData = sessionService.getCurrentUserData(userId);

        BetDTO selectedBet = currentUserData.getBetByBetId(betId);

        if (selectedBet == null){
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error on update data session"));
        }

        MatchGambledDTO matchToRemove = selectedBet.getMatchByMatchId(matchId);

        if (matchToRemove == null){
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Match not founded"));
        }

        currentUserData.removeBet(selectedBet);
        selectedBet.removeMatch(matchToRemove.getGameId());
        currentUserData.addBet(selectedBet);

        if (currentUserData.getListOfBets().size() == 1 && currentUserData.getListOfBets().get(0).getGames().size() == 0){
            currentUserData.setListOfBets(new LinkedList<>());
        }

        return sessionService.updateUserData(currentUserData);
    }
}
