package com.isd.application.dto;

import com.isd.application.commons.CurrencyEnum;

/*
    this class map the followind json
    "place_bet": {
        "userId": 1,
        "betId": 118928932,
        "betValue": 10,
        "currency": "EUR"
    }
 */

public class PlaceBetDTO {
    private Integer userId;
    private Long betId;
    private Integer betValue;
    private CurrencyEnum currency;

    public PlaceBetDTO() {
    }

    public PlaceBetDTO(Integer userId, Long betId, Integer betValue, CurrencyEnum currency) {
        this.userId = userId;
        this.betId = betId;
        this.betValue = betValue;
        this.currency = currency;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getBetId() {
        return betId;
    }

    public void setBetId(Long betId) {
        this.betId = betId;
    }

    public Integer getBetValue() {
        return betValue;
    }

    public void setBetValue(Integer betValue) {
        this.betValue = betValue;
    }

    public CurrencyEnum getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyEnum currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "PlaceBetDTO{" +
                "userId=" + userId +
                ", betId=" + betId +
                ", betValue=" + betValue +
                ", currency='" + currency + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlaceBetDTO that = (PlaceBetDTO) o;

        if (userId != that.userId) return false;
        if (betId != that.betId) return false;
        if (betValue != that.betValue) return false;
        return currency != null ? currency.equals(that.currency) : that.currency == null;
    }
}
