package com.isd.application.dto;


import java.util.List;

public class TeamHistoryDTO {
    private Integer id;
    private String name;
    List<MatchHistoryDto> playedGames;

    public TeamHistoryDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MatchHistoryDto> getMatches() {
        return playedGames;
    }

    public void setMatches(List<MatchHistoryDto> playedGames) {
        this.playedGames = playedGames;
    }
}
