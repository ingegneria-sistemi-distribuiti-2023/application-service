package com.isd.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RemoveMatchDTO {
    private int userId;
    private int gameId;
    private long betId;

}
