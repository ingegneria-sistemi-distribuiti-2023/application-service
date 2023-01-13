package com.isd.application.dto;

import com.isd.application.commons.CurrencyEnum;
import com.isd.application.commons.PlacedBetEnum;

import java.util.List;

public class PlacedBetDTO {
    private int userId;
    private Long betId;
    private int betValue;
    private CurrencyEnum currency;
    private List<MatchGambledDTO> gambledMatches;
    private Double payout;
    private Long ts;
    private PlacedBetEnum status;

    public PlacedBetDTO() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Long getBetId() {
        return betId;
    }

    public void setBetId(Long betId) {
        this.betId = betId;
    }

    public int getBetValue() {
        return betValue;
    }

    public void setBetValue(int betValue) {
        this.betValue = betValue;
    }

    public CurrencyEnum getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyEnum currency) {
        this.currency = currency;
    }

    public List<MatchGambledDTO> getGambledMatches() {
        return gambledMatches;
    }

    public void setGambledMatches(List<MatchGambledDTO> gambledMatches) {
        this.gambledMatches = gambledMatches;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public Double getPayout() {
        return payout;
    }

    public void setPayout(Double payout) {
        this.payout = payout;
    }

    public PlacedBetEnum getStatus() {
        return status;
    }

    public void setStatus(PlacedBetEnum status) {
        this.status = status;
    }
}
