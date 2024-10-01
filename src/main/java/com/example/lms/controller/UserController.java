package com.example.lms.controller;

import com.example.lms.dto.LoginReqDto;
import com.example.lms.dto.LoginResDto;
import com.example.lms.dto.RefreshTokenReqDto;
import com.example.lms.dto.UserDto;
import com.example.lms.entity.UserEntity;
import com.example.lms.response.ResponseModel;
import com.example.lms.service.RefreshTokenService;
import com.example.lms.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/v1/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping(path = "/register")
    ResponseEntity<ResponseModel> registerUser(@RequestBody @Valid UserDto user){
        ResponseModel response=userService.addUser(user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping(path="/login")
    ResponseEntity<ResponseModel<LoginResDto>>loginUser(@RequestBody @Valid LoginReqDto dto){
        ResponseModel<LoginResDto> response=userService.loginUser(dto);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping(path = "/refresh-token")
    ResponseEntity<ResponseModel<LoginResDto>>getNewAccessToken(@RequestBody RefreshTokenReqDto dto){
        ResponseModel<LoginResDto>response=refreshTokenService.refreshToken(dto);
        return new ResponseEntity<>(response,HttpStatus.OK);

    }
}
