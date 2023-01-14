package com.isd.application.dto;

import java.io.Serializable;

import com.isd.application.commons.OutcomeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchGambledDTO implements Serializable{
    private static final long serialVersionUID = 6529685064657757690L;
    private Integer gameId;
    private OutcomeEnum outcome;
    private Double quoteAtTimeOfBet;
    private Long ts;

}
