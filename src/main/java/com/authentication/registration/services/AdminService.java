package com.authentication.registration.services;

import com.authentication.registration.dtos.response.UserSummaryDto;
import com.authentication.registration.enums.Role;

import java.util.List;

public interface AdminService {

    public List<UserSummaryDto> getAllUsers();

    public void updateRole(Long id, Role role);

}
