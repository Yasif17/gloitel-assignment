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

    @Override
    public UserSummaryDto patchUpdate(Long id, UserSummaryDto user) {
        User userEntity = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );

        if(user.getName()!=null){
            userEntity.setName(user.getName());
        }

        if(user.getEmail()!=null){
            userEntity.setEmail(user.getEmail());
        }

        if(user.getRole()!=null){
            userEntity.setRole(Role.valueOf(user.getRole()));
        }

        User savedUserEntity = userRepository.save(userEntity);
        return mapToDto(savedUserEntity);
    }

    public UserSummaryDto mapToDto(User userEntity){
        UserSummaryDto userDto = new UserSummaryDto();
        userDto.setId(userEntity.getId());
        userDto.setName(userEntity.getName());
        userDto.setEmail(userEntity.getEmail());
        userDto.setRole(userEntity.getRole().name());
        userDto.setCreatedAt(userEntity.getCreatedAt());
        return userDto;
    }


}
