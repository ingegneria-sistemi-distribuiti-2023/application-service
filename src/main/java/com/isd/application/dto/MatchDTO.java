package com.isd.application.dto;

import com.isd.application.commons.MatchStatus;
import com.isd.application.commons.OutcomeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchDTO {
    // this DTO contains the match ID, IDs of the teams, the home and away scores, the start time and the end time of the match
    private Integer id;
    private Integer homeTeamId;
    private Integer awayTeamId;
    private String homeTeamName;
    private String awayTeamName;
    private Integer homeTeamScore;
    private Integer awayTeamScore;
    private Double homeWinPayout;
    private Double awayWinPayout;
    private Double drawPayout;
    private Integer inGameMinute;
    private Date startTime;
    private Date endTime;
    private MatchStatus status;

    public Double getPayout(OutcomeEnum outcome){
        switch (outcome) {
            case DRAW:
                return getDrawPayout();
            case AWAY:
                return getAwayWinPayout();
            case HOME:
                return getHomeWinPayout();
            default:
                throw new IllegalArgumentException("Invalid outcome: " + outcome);
        }
    }

}
