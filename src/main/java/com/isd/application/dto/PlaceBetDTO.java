package com.isd.application.dto;

import com.isd.application.commons.CurrencyEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceBetDTO {
    private Integer userId;
    private Long betId;
    private Integer betValue;
    private CurrencyEnum currency;
}
