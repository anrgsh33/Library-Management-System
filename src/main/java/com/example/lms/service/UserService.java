package com.example.lms.service;


import com.example.lms.dto.LoginReqDto;
import com.example.lms.dto.LoginResDto;
import com.example.lms.dto.UserDto;
import com.example.lms.dto.UserResDto;
import com.example.lms.entity.UserEntity;
import com.example.lms.response.ResponseModel;

import java.util.List;

public interface UserService {
    ResponseModel addUser(UserDto user);
    ResponseModel<LoginResDto> loginUser(LoginReqDto user);
    ResponseModel<List<UserResDto>> getAllUsers();
}
