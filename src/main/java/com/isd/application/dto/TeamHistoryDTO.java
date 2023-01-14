package com.isd.application.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamHistoryDTO {
    private Integer id;
    private String name;
    List<MatchHistoryDto> playedGames;

}
