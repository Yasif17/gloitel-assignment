package com.authentication.registration.punch.services;

import com.authentication.registration.punch.dtos.requests.PunchInRequest;
import com.authentication.registration.punch.dtos.requests.PunchOutRequest;
import com.authentication.registration.punch.dtos.responses.PunchResponse;

public interface PunchService {
    public PunchResponse punchIn(PunchInRequest punchInRequest);
    public PunchResponse punchOut(PunchOutRequest punchOutRequest);
}
