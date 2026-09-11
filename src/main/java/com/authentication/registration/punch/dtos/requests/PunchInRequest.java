package com.authentication.registration.punch.dtos.requests;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PunchInRequest {

    private Long userId;
    private Long campId;
    private Double userLatitude;
    private Double userLongitude;
    private String punchInPhoto;

}
