package com.example.lms.controller;

import com.example.lms.dto.LoginReqDto;
import com.example.lms.entity.UserEntity;
import com.example.lms.service.UserService;
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

    @PostMapping(path = "/register")
    ResponseEntity<String> registerUser(@RequestBody UserEntity user){
        String response=userService.addUser(user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping(path="/login")
    ResponseEntity<String>loginUser(@RequestBody LoginReqDto dto){
        String response=userService.loginUser(dto);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


}
