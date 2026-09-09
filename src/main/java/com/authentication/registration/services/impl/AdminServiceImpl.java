package com.authentication.registration.services.impl;

import com.authentication.registration.dtos.response.UserSummaryDto;
import com.authentication.registration.entities.User;
import com.authentication.registration.enums.Role;
import com.authentication.registration.exceptions.UserNotFoundException;
import com.authentication.registration.repositories.UserRepository;
import com.authentication.registration.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserSummaryDto> getAllUsers() {

        List<User> users = userRepository.findAll();

        List<UserSummaryDto> result = new ArrayList<>();

        for(User u:users){
            UserSummaryDto dto = new UserSummaryDto();
            dto.setId(u.getId());
            dto.setName(u.getName());
            dto.setEmail(u.getEmail());
            dto.setRole(u.getRole().name());
            dto.setCreatedAt(u.getCreatedAt());

            result.add(dto);
        }

        return result;


    }

    @Override
    public void updateRole(Long id, Role role) {
        User user = userRepository.findById(id).orElseThrow(
                ()-> new UserNotFoundException("User not found")
        );
        user.setRole(role);
        userRepository.save(user);
    }
}
