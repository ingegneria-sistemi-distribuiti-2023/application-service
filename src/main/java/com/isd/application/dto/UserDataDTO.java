package com.isd.application.dto;

import java.io.Serializable;
import java.util.List;

public class UserDataDTO implements Serializable {
    private static final long serialVersionUID = 6529685099997757690L;
    private Integer userId;
    private List<BetDTO> listOfBets;
    private static int MAX_BETS = 2;

    public UserDataDTO() {
    }

    // getters and setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<BetDTO> getListOfBets() {
        return listOfBets;
    }

    public void setListOfBets(List<BetDTO> listOfBets) {
        this.listOfBets = listOfBets;
    }

    public void addBet(BetDTO bet) throws Exception {
        // Check if the number of bets in the list is less than or equal to 3
        if (listOfBets.size() <= MAX_BETS) {
            // Iterate through the list of bets
            if (listOfBets.contains(bet)){
                throw new Exception("Bet is the same");
            }

            listOfBets.add(bet);
        } else {
            throw new Exception("Cannot add more than 3 bets to the list.");
        }
    }

    public void removeBet(BetDTO bet) throws Exception {
        // Check if the bet is present in the listOfBets
        if (listOfBets.contains(bet)) {
            // If it is, remove the bet from the listOfBets
            listOfBets.remove(bet);
        } else {
            throw new Exception("Bet not found in the list.");
        }
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

