package com.isd.application.controller;

import com.isd.application.dto.MatchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@RestController
@RequestMapping("/app/game")
public class GameController {

    @Autowired
    RestTemplate restTemplate;

    @Value("${game.service.url}")
    String gameServiceUrl;

    @GetMapping(value="/")
    public ResponseEntity<List<MatchDTO>> getAllGames() throws Exception{
        try {
            ResponseEntity<List<MatchDTO>> response = restTemplate.exchange(
                    gameServiceUrl + "/match/all", HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<MatchDTO>>() {});

            // verify status code of request
            if (response.getStatusCode() == HttpStatus.OK) {
                return ResponseEntity.ok(response.getBody());
            } else {
                // handle errors or timeout here
                return ResponseEntity.status(response.getStatusCode()).build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("/{id}")
    public void getGameDetails(@PathVariable("id") String gameId){

    }


    @GetMapping("/team/{name}")
    public void getHistoryOfTeam(
            @PathVariable("name") String teamName){

    }
}
