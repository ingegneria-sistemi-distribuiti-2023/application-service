package com.isd.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBalanceDTO {
    private Integer userId;
    private String username;
    private Float cashableAmount;
    private Float bonusAmount;
    private Boolean enabled;
}
