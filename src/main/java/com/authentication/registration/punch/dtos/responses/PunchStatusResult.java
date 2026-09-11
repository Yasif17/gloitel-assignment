package com.authentication.registration.punch.dtos.responses;

import com.authentication.registration.punch.enums.PunchStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PunchStatusResult {
    private PunchStatus punchStatus;
    private long deductedMinutes;
}
