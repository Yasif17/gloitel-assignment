package com.authentication.registration.punch.dtos.responses;

import com.authentication.registration.punch.enums.PunchStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PunchResponse {

    private Long punchId;

    private PunchStatus punchStatus;

    private String message;

    private LocalTime punchTime;

}
