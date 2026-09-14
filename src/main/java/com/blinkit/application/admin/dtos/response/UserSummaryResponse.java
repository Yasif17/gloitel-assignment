package com.blinkit.application.admin.dtos.response;

import java.time.LocalDateTime;

public record UserSummaryResponse(
        Long id,
        String name,
        String email,
        String role,
        LocalDateTime createdAt
) {}