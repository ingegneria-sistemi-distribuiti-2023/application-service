package com.isd.application.dto;

/** DTO usato per visualizzare tutte le informazioni relative ad un utente **/
public class UserBalanceDTO {
    private Integer userId;
    private String username;
    private Float cashableAmount;
    private Float bonusAmount;
    private Boolean enabled;

    public UserBalanceDTO() {
    }

    public UserBalanceDTO(Integer userId, String username, Float cashableAmount, Float bonusAmount, Boolean enabled) {
        this.userId = userId;
        this.username = username;
        this.cashableAmount = cashableAmount;
        this.bonusAmount = bonusAmount;
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Float getCashableAmount() {
        return cashableAmount;
    }

    public void setCashableAmount(Float cashableAmount) {
        this.cashableAmount = cashableAmount;
    }

    public Float getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(Float bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getEnabled() {
        return enabled;
    }
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
