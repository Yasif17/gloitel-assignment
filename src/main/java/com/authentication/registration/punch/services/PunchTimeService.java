package com.authentication.registration.punch.services;

import com.authentication.registration.punch.dtos.responses.PunchStatusResult;

import java.time.LocalTime;

public interface PunchTimeService {

    public boolean isWithinTimeWindow(LocalTime punchTime,LocalTime campStartTime,LocalTime campEndTime,boolean isPunchIn);

    public PunchStatusResult calculatePunchStatus(LocalTime punchInTime, LocalTime campStartTime);

}
