package com.isd.application.dto;

/*
 this class map the followind json
 "remove_match": {
        "userId": 1,
        "gameId": 1,
        "betId": 118928932
    }
 */

public class RemoveMatchDTO {
    private int userId;
    private int gameId;
    private long betId;

    public RemoveMatchDTO() {
    }

    public RemoveMatchDTO(int userId, int gameId, long betId) {
        this.userId = userId;
        this.gameId = gameId;
        this.betId = betId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public long getBetId() {
        return betId;
    }

    public void setBetId(long betId) {
        this.betId = betId;
    }

    @Override
    public String toString() {
        return "RemoveMatchDTO{" +
                "userId=" + userId +
                ", gameId=" + gameId +
                ", betId=" + betId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RemoveMatchDTO that = (RemoveMatchDTO) o;

        if (userId != that.userId) return false;
        if (gameId != that.gameId) return false;
        return betId == that.betId;
    }
}
