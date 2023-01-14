package com.isd.application.domain;

import com.isd.application.commons.CurrencyEnum;
import com.isd.application.commons.PlacedBetEnum;
import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "placed_bets")
public class PlacedBet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    private Integer amount;
    @Enumerated(EnumType.STRING)
    private CurrencyEnum currency;
    @Enumerated(EnumType.STRING)
    private PlacedBetEnum status;
    private Long ts;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "placedBet")
    private List<PlacedBetMatch> matches;

    public PlacedBet() {
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

    public PlacedBetEnum getStatus() {
        return status;
    }

    public void setStatus(PlacedBetEnum status) {
        this.status = status;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public List<PlacedBetMatch> getMatches() {
        return matches;
    }

    public void setMatches(List<PlacedBetMatch> matches) {
        this.matches = matches;
    }
}