package com.isd.application.dto;

import com.isd.application.commons.CurrencyEnum;
import com.isd.application.commons.PlacedBetEnum;

import java.util.List;

public class PlacedBetDTO {
    private Integer id;
    private Integer userId;
    private Long betId;
    private Integer amount;
    private CurrencyEnum currency;
    private List<MatchGambledDTO> gambledMatches;
    private Double payout;
    private Long ts;
    private PlacedBetEnum status;

    public PlacedBetDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
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
