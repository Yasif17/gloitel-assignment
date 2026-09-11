package com.authentication.registration.punch.dtos.requests;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PunchOutRequest {

    private Long userId;
    private Long campId;
    private Double userLatitude;
    private Double userLongitude;
    private String punchOutPhoto; // base64

}
