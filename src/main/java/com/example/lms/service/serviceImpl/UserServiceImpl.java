package com.example.lms.service.serviceImpl;

import com.example.lms.dto.LoginReqDto;
import com.example.lms.dto.UserDto;
import com.example.lms.dto.UserResDto;
import com.example.lms.entity.UserEntity;
import com.example.lms.exception.CustomServiceException;
import com.example.lms.repository.UserRepository;
import com.example.lms.response.ResponseModel;
import com.example.lms.service.UserService;
import com.example.lms.util.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public ResponseModel addUser(UserDto user) {
        try {
            UserEntity userEntity=new UserEntity();
            userEntity.setEmail(user.getEmail());
            userEntity.setName(user.getName());
            userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
            userEntity.setRole(user.getRole());
            userEntity.setLastActivity(LocalDateTime.now());

            userRepository.save(userEntity);

            return new ResponseModel("user added successfully",HttpStatus.CREATED,null);

        }catch(Exception e){
            throw new CustomServiceException(e.getMessage());
        }
    }

    @Override
    public ResponseModel<String> loginUser(LoginReqDto user){
        try {

            log.info("email:{} password:{}",user.getEmail(),user.getPassword());
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

            Optional<UserEntity> userOptional = userRepository.findByEmail(user.getEmail());
            if (userOptional.isPresent()) {
                log.info(userOptional.get().getName());
                String jwt = jwtUtils.generateToken(user.getEmail());
               //Upating user last activity on login also
                UserEntity userEntity=userOptional.get();
                userEntity.setLastActivity(LocalDateTime.now());
                userRepository.save(userEntity);

                return new ResponseModel("User successfully logged in", HttpStatus.OK,jwt.toString());

            } else {
                throw new RuntimeException("Invalid credentials");
            }
        } catch (Exception e) {
            throw new CustomServiceException("Unexpected error during user login");
        }
    }

    @Override
    public ResponseModel<List<UserResDto>> getAllUsers() {
        List<UserEntity>userList=  userRepository.findAll();
        List<UserResDto> userResDtoList = userList.stream()
                .map(user -> new UserResDto(user.getId(), user.getName(), user.getEmail()))
                .collect(Collectors.toList());
        return new ResponseModel<>("Users retreived successfully",HttpStatus.OK , userResDtoList);
    }
}
