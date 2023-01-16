package com.isd.application.controller;

import com.isd.application.dto.MatchDTO;
import com.isd.application.dto.TeamHistoryDTO;
import com.isd.application.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/app/public")
public class MatchController {
    private final GameService gameService;

    public MatchController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping(value="/match")
    public ResponseEntity<List<MatchDTO>> getAllMatches() throws Exception {
        return new ResponseEntity<>(gameService.getAllMatches(), HttpStatus.OK);
    }

    @GetMapping("/match/{id}")
    public ResponseEntity<MatchDTO> getMatchDetails(@PathVariable("id") Integer gameId) throws Exception{
        return new ResponseEntity<>(gameService.getMatchDetail(gameId), HttpStatus.OK);
    }

    @GetMapping("/team/{id}")
    public ResponseEntity<TeamHistoryDTO> getHistoryOfTeam(@PathVariable("id") Integer teamId) throws Exception {
        return new ResponseEntity<>(gameService.getHistoryOfTeam(teamId), HttpStatus.OK);
    }
}
