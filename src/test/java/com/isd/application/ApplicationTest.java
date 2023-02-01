package com.isd.application;

import com.isd.application.commons.CurrencyEnum;
import com.isd.application.commons.OutcomeEnum;
import com.isd.application.commons.PlacedBetEnum;
import com.isd.application.commons.TransactionStatus;
import com.isd.application.commons.error.CustomServiceException;
import com.isd.application.domain.PlacedBet;
import com.isd.application.domain.PlacedBetMatch;
import com.isd.application.dto.*;
import com.isd.application.repository.PlacedBetMatchRepository;
import com.isd.application.repository.PlacedBetRepository;
import com.isd.application.service.AuthenticationService;
import com.isd.application.service.PlacedBetService;
import com.isd.application.service.SessionService;
import org.junit.Before;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private SessionService sessionService;

    @Mock
    private PlacedBetRepository placedBetRepository;

    @Mock
    private PlacedBetMatchRepository placedBetMatchRepository;

    @InjectMocks
    private PlacedBetService placedBetService;

    private PlacedBetDTO placedBetDTO;
    private PlacedBet placedBet;
    private UserDataDTO currentSession;
    private UserBalanceDTO userBalance;

    public static UserDataDTO generateDummyData() {
        UserDataDTO userData = new UserDataDTO();
        userData.setUserId(12345);
        userData.setSessionId("abcdefg");
        userData.setListOfBets(new ArrayList<>());
        return userData;
    }

    public MatchGambledDTO generateDummyMatch() {
        MatchGambledDTO match = new MatchGambledDTO();
        Random random = new Random();
        match.setGameId(random.nextInt(10000));
        match.setOutcome(OutcomeEnum.values()[random.nextInt(OutcomeEnum.values().length)]);
        match.setQuoteAtTimeOfBet(random.nextDouble());
        match.setTs(new Date().getTime());
        return match;
    }

    public BetDTO generateDummyBet() {
        BetDTO bet = new BetDTO();
        Random random = new Random();
        bet.setBetValue(random.nextInt(3,4));
        bet.setCurrency(CurrencyEnum.values()[random.nextInt(CurrencyEnum.values().length)]);
        bet.setTs(new Date().getTime());
        List<MatchGambledDTO> games = new ArrayList<>();
        for(int i = 0; i < BetDTO.MAX_MATCH; i++) {
            MatchGambledDTO match = generateDummyMatch();
            games.add(match);
        }
        bet.setGames(games);
        return bet;
    }

    public static PlacedBetDTO generateRandomPlacedBetDTO() {
        PlacedBetDTO placedBetDTO = new PlacedBetDTO();
        placedBetDTO.setId(new Random().nextInt());
        placedBetDTO.setUserId(new Random().nextInt());
        placedBetDTO.setAmount(new Random().nextInt());
        placedBetDTO.setCurrency(CurrencyEnum.values()[new Random().nextInt(CurrencyEnum.values().length)]);
        placedBetDTO.setGambledMatches(generateRandomListOfMatchGambledDTO());
        placedBetDTO.setPayout(new Random().nextDouble());
        placedBetDTO.setTs(System.currentTimeMillis());
        placedBetDTO.setStatus(PlacedBetEnum.values()[new Random().nextInt(PlacedBetEnum.values().length)]);
        return placedBetDTO;
    }

    private static List<MatchGambledDTO> generateRandomListOfMatchGambledDTO() {
        int size = new Random().nextInt(3) + 1;
        List<MatchGambledDTO> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(generateRandomMatchGambledDTO());
        }
        return list;
    }

    private static MatchGambledDTO generateRandomMatchGambledDTO() {
        MatchGambledDTO matchGambledDTO = new MatchGambledDTO();
        matchGambledDTO.setGameId(new Random().nextInt());
        matchGambledDTO.setOutcome(OutcomeEnum.values()[new Random().nextInt(OutcomeEnum.values().length)]);
        matchGambledDTO.setQuoteAtTimeOfBet(new Random().nextDouble());
        matchGambledDTO.setTs(System.currentTimeMillis());
        return matchGambledDTO;
    }

    public PlacedBetMatch generateRandomPlacedBetMatch(PlacedBet placedBet, Integer matchId) {
        PlacedBetMatch placedBetMatch = new PlacedBetMatch();
        placedBetMatch.setPlacedBet(placedBet);
        placedBetMatch.setMatchId(matchId);
        placedBetMatch.setOutcome(OutcomeEnum.values()[new Random().nextInt(OutcomeEnum.values().length)]);
        placedBetMatch.setQuote(new Random().nextDouble());
        placedBetMatch.setTs(new Date().getTime());
        return placedBetMatch;
    }

    /**
     * this method is called before each test, it generates the dummy data used
     */
    @Before
    public void setUp() {
        currentSession = generateDummyData();

        placedBetDTO = new PlacedBetDTO();
        placedBet = new PlacedBet();

        userBalance = new UserBalanceDTO();
        userBalance.setUserId(currentSession.getUserId());
        userBalance.setEnabled(true);
        userBalance.setBonusAmount(0f);
        userBalance.setCashableAmount(0f);
    }

    /**
     * this method tests the PlacedBetService's save method
     */
    @Test
    public void test_saveShouldReturnPlacedbetDtoFromService() {
        when(placedBetRepository.save(any(PlacedBet.class))).thenReturn(placedBet);

        List<MatchGambledDTO> list = new LinkedList<>();
        list.add(generateRandomMatchGambledDTO());
        list.add(generateRandomMatchGambledDTO());

        placedBetDTO.setGambledMatches(list);

        placedBetDTO = placedBetService.save(placedBetDTO);
        assertEquals(placedBet.getId(), placedBetDTO.getId());
    }

    /**
     * this method tests the PlacedBetService's getAllBetsByUserId method
     * @throws Exception
     */
    @Test
    public void test_getAllBetsByUserIdShouldReturnSamePlacebetService() throws Exception {
        List<PlacedBet> placedBets = new LinkedList<>();
        placedBet.setUserId(currentSession.getUserId());
        placedBet.setAmount(10);
        placedBet.setCurrency(CurrencyEnum.EUR);
        placedBet.setStatus(PlacedBetEnum.PLAYED);
        placedBet.setTs(1L);
        placedBet.setId(1);
        List<PlacedBetMatch> matches = new LinkedList<>();
        matches.add(generateRandomPlacedBetMatch(placedBet, 1));
        placedBet.setMatches(matches);

        placedBets.add(placedBet);

        when(placedBetRepository.findAllByUserId(currentSession.getUserId())).thenReturn(placedBets);

        List<PlacedBetDTO> result = placedBetService.getAllBetsByUserId(currentSession.getUserId());
        assertEquals(1, result.size());
        assertEquals(placedBets.get(0), placedBet);
    }

    /**
     * this method tests the PlacedBetService's placeBet method, it should return a played status
     * @throws Exception
     */
     @Test
     public void test_placeBetShouldReturnPlayedStatus() throws Exception {
         when(authenticationService.getUserInfo(currentSession.getUserId(), "jwt")).thenReturn(userBalance);
         when(sessionService.getCurrentUserData(currentSession.getUserId())).thenReturn(currentSession);

         List<BetDTO> list = new LinkedList<>();
         list.add(generateDummyBet());

         placedBet.setUserId(currentSession.getUserId());
         placedBet.setAmount(10);
         placedBet.setCurrency(CurrencyEnum.EUR);

         List<PlacedBetMatch> matches = new LinkedList<>();
         matches.add(generateRandomPlacedBetMatch(placedBet, 1));
         matches.add(generateRandomPlacedBetMatch(placedBet, 2));

         placedBet.setMatches(matches);

         currentSession.setListOfBets(list);

         when(placedBetRepository.save(any(PlacedBet.class))).thenReturn(placedBet);
         when(placedBetMatchRepository.save(any(PlacedBetMatch.class))).thenReturn(placedBet.getMatches().get(0));

         PlaceBetDTO dto = new PlaceBetDTO();
         dto.setUserId(currentSession.getUserId());
         dto.setBetValue(3);
         dto.setBetId(list.get(0).getTs());
         dto.setCurrency(CurrencyEnum.USD);

         userBalance.setCashableAmount(5f);

         when(authenticationService.withdraw(any(TransactionRequestDTO.class))).thenReturn(new TransactionResponseDTO(TransactionStatus.CLOSED, "Success", new Date()));
         // update cashableAmount after bet

         PlacedBetDTO result = placedBetService.placeBet(dto, "jwt");

         assertEquals(PlacedBetEnum.PLAYED, result.getStatus());
     }

    /**
     * this method tests the PlacedBetService's placeBet method when the bet value is less then 2
     * @throws Exception
     */
    @Test(expected = CustomServiceException.class)
    public void test_placeBetShouldReturnErrorWhenBetValueIsLessThen2() throws Exception {
        PlaceBetDTO dto = new PlaceBetDTO();
        dto.setUserId(currentSession.getUserId());
        dto.setBetValue(1);
        dto.setBetId(1L);
        dto.setCurrency(CurrencyEnum.USD);
        PlacedBetDTO placedBet = placedBetService.placeBet(dto, "jwt");

        assertEquals(placedBet.getAmount(), dto.getBetValue());
        assertEquals(placedBet.getUserId(), dto.getUserId());
    }

    /**
     * this method tests the PlacedBetService's placeBet method when there is no user id in the dto
     * @throws Exception
     */
    @Test(expected = CustomServiceException.class)
    public void test_placeBetWithNoUserIdShouldReturnCustomServiceException() throws Exception {
        PlaceBetDTO dto = new PlaceBetDTO();

        BetDTO bet = generateDummyBet();

        dto.setUserId(null);
        dto.setBetValue(bet.getBetValue());
        dto.setBetId(bet.getTs());

        dto.setCurrency(CurrencyEnum.USD);
        placedBetService.placeBet(dto, "jwt");
    }

    /**
     * this method tests the PlacedBetService's placeBet method when the cashable amount is 0
     * @throws Exception
     */
     @Test(expected = CustomServiceException.class)
     public void test_placeBetWithNoCashableAmountShouldReturnError() throws Exception {
         when(authenticationService.getUserInfo(currentSession.getUserId(), "jwt")).thenReturn(userBalance);

         BetDTO bet = generateDummyBet();

         PlaceBetDTO dto = new PlaceBetDTO();
         dto.setUserId(currentSession.getUserId());
         dto.setBetValue(bet.getBetValue());
         dto.setBetId(bet.getTs());
         dto.setCurrency(CurrencyEnum.USD);
         userBalance.setCashableAmount(0f);
         placedBetService.placeBet(dto, "jwt");
     }


}
