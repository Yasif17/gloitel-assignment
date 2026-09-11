package com.authentication.registration.punch.services;

import com.authentication.registration.punch.dtos.responses.PunchStatusResult;
import com.authentication.registration.punch.enums.PunchStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;

@Service
public class PunchTimeServiceImpl implements PunchTimeService{
    private static final int GRACE_PERIOD_MINUTES = 30;

    // GATEKEEPER — can he punch at all right now?
    @Override
    public boolean isWithinTimeWindow(LocalTime punchTime, LocalTime campStart, LocalTime campEnd, boolean isPunchIn) {
        if (isPunchIn) {
            // can't punch-in after camp has closed
            return !punchTime.isAfter(campEnd);
        } else {
            // punch-out just needs camp to have started
            return !punchTime.isBefore(campStart);
        }
    }

    // BUSINESS LOGIC — was he late, and how many minutes to deduct?
    @Override
    public PunchStatusResult calculatePunchStatus(LocalTime punchInTime, LocalTime campStart) {
        LocalTime graceEnd = campStart.plusMinutes(GRACE_PERIOD_MINUTES);

        if (!punchInTime.isAfter(graceEnd)) {
            return new PunchStatusResult(PunchStatus.ON_TIME, 0);
        }

        long lateMinutes = Duration.between(campStart, punchInTime).toMinutes();
        return new PunchStatusResult(PunchStatus.LATE, lateMinutes);
    }
}
