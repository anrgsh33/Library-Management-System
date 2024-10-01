package com.example.lms.service;


import com.example.lms.dto.LoginResDto;
import com.example.lms.dto.RefreshTokenReqDto;
import com.example.lms.entity.UserEntity;
import com.example.lms.response.ResponseModel;

public interface RefreshTokenService {
    String getRefreshToken(String username);
    void deleteRefreshToken(int userId);
    ResponseModel<LoginResDto>refreshToken(RefreshTokenReqDto dto);
}
