package com.authentication.registration.advices;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

    private String message;
    private int status;
    private LocalDateTime timeStamp;
    Map<String,String> fieldErrors;

}
