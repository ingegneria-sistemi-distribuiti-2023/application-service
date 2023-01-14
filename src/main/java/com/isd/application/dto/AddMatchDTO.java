package com.isd.application.dto;
import com.isd.application.commons.OutcomeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddMatchDTO {
    private int userId;
    private int gameId;
    private OutcomeEnum outcome;
    private Long betId;

}
