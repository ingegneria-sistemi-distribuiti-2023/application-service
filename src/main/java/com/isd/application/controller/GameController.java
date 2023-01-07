package com.isd.application.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/game")
public class GameController {


    @GetMapping(value="/")
    public void getAllGames(){

    }

    @GetMapping("/{id}")
    public void getGameDetails(
            @PathVariable("id") String gameId){

    }


    @GetMapping("/team/{name}")
    public void getHistoryOfTeam(
            @PathVariable("name") String teamName){

    }
}
