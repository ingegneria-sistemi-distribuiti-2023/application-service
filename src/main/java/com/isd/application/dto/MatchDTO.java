package com.isd.application.dto;

import com.isd.application.commons.MatchStatus;

import java.util.Date;

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

    public MatchDTO() {
    }

    public MatchDTO(Integer id, Integer homeTeamId, Integer awayTeamId, String homeTeamName, String awayTeamName, Integer homeTeamScore, Double homeWinPayout, Double awayWinPayout, Double drawPayout, Integer inGameMinute, Integer awayTeamScore, Date startTime, Date endTime, MatchStatus status) {
        this.id = id;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
        this.homeTeamScore = homeTeamScore;
        this.awayTeamScore = awayTeamScore;
        this.homeWinPayout = homeWinPayout;
        this.awayWinPayout = awayWinPayout;
        this.drawPayout = drawPayout;
        this.inGameMinute = inGameMinute;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(Integer homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public Integer getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(Integer awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public void setAwayTeamName(String awayTeamName) {
        this.awayTeamName = awayTeamName;
    }

    public Integer getHomeTeamScore() {
        return homeTeamScore;
    }

    public void setHomeTeamScore(Integer homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
    }

    public Integer getAwayTeamScore() {
        return awayTeamScore;
    }

    public void setAwayTeamScore(Integer awayTeamScore) {
        this.awayTeamScore = awayTeamScore;
    }

    public Double getHomeWinPayout() {
        return homeWinPayout;
    }

    public void setHomeWinPayout(Double homeWinPayout) {
        this.homeWinPayout = homeWinPayout;
    }

    public Double getAwayWinPayout() {
        return awayWinPayout;
    }

    public void setAwayWinPayout(Double awayWinPayout) {
        this.awayWinPayout = awayWinPayout;
    }

    public Double getDrawPayout() {
        return drawPayout;
    }

    public void setDrawPayout(Double drawPayout) {
        this.drawPayout = drawPayout;
    }

    public Integer getInGameMinute() {
        return inGameMinute;
    }

    public void setInGameMinute(Integer inGameMinute) {
        this.inGameMinute = inGameMinute;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MatchDto [id=" + id + ", homeTeamId=" + homeTeamId + ", awayTeamId=" + awayTeamId + ", homeTeamName="
                + homeTeamName + ", awayTeamName=" + awayTeamName + ", homeTeamScore=" + homeTeamScore
                + ", awayTeamScore=" + awayTeamScore + ", homeWinPayout=" + homeWinPayout + ", awayWinPayout="
                + awayWinPayout + ", drawPayout=" + drawPayout + ", inGameMinute=" + inGameMinute + ", startTime="
                + startTime + ", endTime=" + endTime + ", status=" + status + "]";
    }

    // override the equals method to compare two MatchDto objects
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MatchDTO other = (MatchDTO) obj;
        if (awayTeamId != other.awayTeamId)
            return false;
        if (awayTeamName == null) {
            if (other.awayTeamName != null)
                return false;
        } else if (!awayTeamName.equals(other.awayTeamName))
            return false;
        if (awayTeamScore != other.awayTeamScore)
            return false;
        if (endTime == null) {
            if (other.endTime != null)
                return false;
        } else if (!endTime.equals(other.endTime))
            return false;
        if (homeTeamId != other.homeTeamId)
            return false;
        if (homeTeamName == null) {
            if (other.homeTeamName != null)
                return false;
        } else if (!homeTeamName.equals(other.homeTeamName))
            return false;
        if (homeTeamScore != other.homeTeamScore)
            return false;
        if (id != other.id)
            return false;
        if (inGameMinute != other.inGameMinute)
            return false;
        if (startTime == null) {
            if (other.startTime != null)
                return false;
        } else if (!startTime.equals(other.startTime))
            return false;
        if (status != other.status)
            return false;
        // check homeWinPayout
        if (homeWinPayout == null) {
            if (other.homeWinPayout != null)
                return false;
        } else if (!homeWinPayout.equals(other.homeWinPayout))
            return false;
        // check awayWinPayout
        if (awayWinPayout == null) {
            if (other.awayWinPayout != null)
                return false;
        } else if (!awayWinPayout.equals(other.awayWinPayout))
            return false;
        // check drawPayout
        if (drawPayout == null) {
            if (other.drawPayout != null)
                return false;
        } else if (!drawPayout.equals(other.drawPayout))
            return false;
        return true;
    }
}
