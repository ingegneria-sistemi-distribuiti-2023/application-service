package com.isd.application.dto;
// this class is a mapping for the following JSON:
/*
  "user_data": {
        "userId": 1,
        "list_of_bets": [
            {
                "betValue": 10,
                "currency": "EUR",
                "games": [
                    // … list of games {gameId, outcome, quoteAtTimeOfBet} …
                    {
                        "gameId": 1,
                        "outcome": "1", // TODO: creare enum per gli outcome (1, X, 2)
                        "quoteAtTimeOfBet": 1.73,
                        "ts": 18928932
                    }
                ],
                "ts": 18928932
            }
        ]
    },
 */
import java.util.List;

public class UserDataDTO {
    private int userId;
    private List<BetDTO> listOfBets;

    // getters and setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<BetDTO> getListOfBets() {
        return listOfBets;
    }

    public void setListOfBets(List<BetDTO> listOfBets) {
        this.listOfBets = listOfBets;
    }

    // toString override
    @Override
    public String toString() {
        return "UserData{" +
                "userId=" + userId +
                ", listOfBets=" + listOfBets +
                '}';
    }

    // equals override
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDataDTO that = (UserDataDTO) o;

        if (userId != that.userId) return false;
        return listOfBets != null ? listOfBets.equals(that.listOfBets) : that.listOfBets == null;
    }
}

