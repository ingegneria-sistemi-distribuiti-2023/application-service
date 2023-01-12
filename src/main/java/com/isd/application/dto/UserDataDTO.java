package com.isd.application.dto;

import com.isd.application.dto.BetDTO;

import java.io.Serializable;
import java.util.List;

public class UserDataDTO implements Serializable {
    private static final long serialVersionUID = 6529685099997757690L;
    private Integer userId;
    private List<BetDTO> listOfBets;
    private String sessionId;
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

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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
        return "UserDataDTO{" +
                "userId=" + userId +
                ", listOfBets=" + listOfBets +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }

    // equals override
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDataDTO that = (UserDataDTO) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        return listOfBets != null ? listOfBets.equals(that.listOfBets) : that.listOfBets == null;
    }

    // hashCode override
    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (listOfBets != null ? listOfBets.hashCode() : 0);
        return result;
    }
}

