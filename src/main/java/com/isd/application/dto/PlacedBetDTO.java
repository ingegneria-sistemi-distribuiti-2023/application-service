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

}
