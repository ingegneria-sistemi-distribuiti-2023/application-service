package com.isd.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDTO {
    private Integer userId;
    private Float amount;
    private String circuit;

}
