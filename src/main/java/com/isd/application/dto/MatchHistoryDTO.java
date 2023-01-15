package com.isd.application.dto;

import com.isd.application.commons.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchHistoryDTO {
    private Integer id;
    private Integer homeTeamId;
    private Integer awayTeamId;
    private String homeTeamName;
    private String awayTeamName;
    private Integer homeTeamScore;
    private Integer awayTeamScore;
    private Date startTime;
    private Date endTime;
    private MatchStatus status;
}
