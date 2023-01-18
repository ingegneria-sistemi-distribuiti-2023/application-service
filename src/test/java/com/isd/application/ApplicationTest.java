package com.isd.application;

import com.isd.application.commons.error.CustomServiceException;
import com.isd.application.domain.PlacedBet;
import com.isd.application.dto.*;
import com.isd.application.repository.PlacedBetMatchRepository;
import com.isd.application.repository.PlacedBetRepository;
import com.isd.application.service.AuthenticationService;
import com.isd.application.service.GameService;
import com.isd.application.service.PlacedBetService;
import com.isd.application.service.SessionService;
import org.junit.Before;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.LinkedList;
import java.util.List;

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
    private GameService gameService;

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
    private UserBalanceDTO user;
    private BetDTO selectedBet;

    @Before
    public void setUp() {
        placedBetDTO = new PlacedBetDTO();
        placedBet = new PlacedBet();
        currentSession = new UserDataDTO();
        user = new UserBalanceDTO();
        selectedBet = new BetDTO();
    }

//    @Test
//    public void testSave() {
//        when(placedBetRepository.save(any(PlacedBet.class))).thenReturn(placedBet);
//        placedBetDTO = placedBetService.save(placedBetDTO);
//        assertEquals(placedBet.getId(), placedBetDTO.getId());
//    }

    @Test(expected = CustomServiceException.class)
    public void testGetByBetIdNotFound() throws Exception {
        when(placedBetRepository.findOneById(1)).thenReturn(null);
        placedBetService.getByBetId(1);
    }

//    @Test
//    public void testGetAllBetsByUserId() throws Exception {
//        List<PlacedBet> placedBets = new LinkedList<>();
//        placedBets.add(placedBet);
//        when(placedBetRepository.findAllByUserId(1)).thenReturn(placedBets);
//        List<PlacedBetDTO> result = placedBetService.getAllBetsByUserId(1);
//        assertNotNull(result);
//        assertEquals(1, result.size());
//    }

    @Test(expected = CustomServiceException.class)
    public void testGetAllBetsByUserIdInvalidUserId() throws Exception {
        placedBetService.getAllBetsByUserId(null);
    }

//    @Test
//    public void testPlaceBet() throws Exception {
//        placedBetDTO.setStatus(PlacedBetEnum.PROCESSING);
//        when(authenticationService.getUserInfo(1, "jwt")).thenReturn(user);
//        when(sessionService.getCurrentUserData(1)).thenReturn(currentSession);
//        currentSession.setBetByBetId(1L, selectedBet);
//        when(placedBetRepository.save(any(PlacedBet.class))).thenReturn(placedBet);
//        PlaceBetDTO dto = new PlaceBetDTO();
//        dto.setUserId(1);
//        dto.setBetValue(3);
//        dto.setBetId(1L);
//        dto.setCurrency(CurrencyEnum.USD);
//        user.setCashableAmount(5);
//        PlacedBetDTO result = placedBetService.placeBet(dto, "jwt");
//        assertEquals(PlacedBetEnum.PROCESSING, result.getStatus());
//    }


//    @Test(expected = CustomServiceException.class)
//    public void testPlaceBetInvalidCurrency() throws Exception {
//        PlaceBetDTO dto = new PlaceBetDTO();
//        dto.setUserId(1);
//        dto.setBetValue(3);
//        dto.setBetId(1L);
//        dto.setCurrency(null);
//        placedBetService.placeBet(dto, "jwt");
//    }

//    @Test(expected = CustomServiceException.class)
//    public void testPlaceBetInvalidBetValue() throws Exception {
//        PlaceBetDTO dto = new PlaceBetDTO();
//        dto.setUserId(1);
//        dto.setBetValue(1);
//        dto.setBetId(1L);
//        dto.setCurrency(CurrencyEnum.USD);
//        placedBetService.placeBet(dto, "jwt");
//    }

//    @Test(expected = CustomServiceException.class)
//    public void testPlaceBetInvalidUserId() throws Exception {
//        PlaceBetDTO dto = new PlaceBetDTO();
//        dto.setUserId(null);
//        dto.setBetValue(3);
//        dto.setBetId(1L);
//        dto.setCurrency(CurrencyEnum.USD);
//        placedBetService.placeBet(dto, "jwt");
//    }

//    @Test(expected = CustomServiceException.class)
//    public void testPlaceBetInsufficientCash() throws Exception {
//        PlaceBetDTO dto = new PlaceBetDTO();
//        dto.setUserId(1);
//        dto.setBetValue(5);
//        dto.setBetId(1L);
//        dto.setCurrency(CurrencyEnum.USD);
//        user.setCashableAmount(3);
//        placedBetService.placeBet(dto, "jwt");
//    }

//    @Test(expected = CustomServiceException.class)
//    public void testPlaceBetBetNotFound() throws Exception {
//        PlaceBetDTO dto = new PlaceBetDTO();
//        dto.setUserId(1);
//        dto.setBetValue(5);
//        dto.setBetId(1L);
//        dto.setCurrency(CurrencyEnum.USD);
//        user.setCashableAmount(5f);
//        currentSession.addBet();
//        currentSession.setBetByBetId(1L, null);
//        placedBetService.placeBet(dto, "jwt");
//    }

}
