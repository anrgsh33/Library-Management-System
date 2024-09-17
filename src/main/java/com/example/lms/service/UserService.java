package com.example.lms.service;


import com.example.lms.dto.LoginReqDto;
import com.example.lms.entity.UserEntity;

import java.util.List;

public interface UserService {
    String addUser(UserEntity userEntity);
    String loginUser(LoginReqDto user);
    List<UserEntity> getAllUsers();
}
