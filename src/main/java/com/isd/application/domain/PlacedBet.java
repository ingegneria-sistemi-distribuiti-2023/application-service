package com.isd.application.domain;

import com.isd.application.commons.CurrencyEnum;
import com.isd.application.commons.PlacedBetEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
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

}