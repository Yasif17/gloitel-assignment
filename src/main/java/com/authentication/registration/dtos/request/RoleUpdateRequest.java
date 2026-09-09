package com.authentication.registration.dtos.request;

import com.authentication.registration.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleUpdateRequest {

    @NotNull(message = "Role cannot be null")
    private Role role;
}
