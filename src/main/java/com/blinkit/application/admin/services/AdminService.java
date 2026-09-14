package com.blinkit.application.admin.services;

import com.blinkit.application.userAuth.dtos.response.UserSummaryDto;
import com.blinkit.application.userAuth.enums.Role;

import java.util.List;

public interface AdminService {

    public List<UserSummaryDto> getAllUsers();

    public void updateRole(Long id, Role role);

}
