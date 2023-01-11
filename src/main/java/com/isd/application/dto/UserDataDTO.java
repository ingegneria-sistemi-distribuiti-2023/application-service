package com.isd.application.dto;

import java.io.Serializable;
import java.util.List;

public class UserDataDTO implements Serializable {
    private static final long serialVersionUID = 6529685099997757690L;
    private Integer userId;
    private List<BetDTO> listOfBets;

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

