package com.authentication.registration.authRegister.services;

import com.authentication.registration.authRegister.dtos.response.UserSummaryDto;
import com.authentication.registration.authRegister.enums.Role;

import java.util.List;

public interface AdminService {

    public List<UserSummaryDto> getAllUsers();

    public void updateRole(Long id, Role role);

    public UserSummaryDto patchUpdate(Long id,UserSummaryDto user );

}
