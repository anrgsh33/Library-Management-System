package com.example.lms.service.serviceImpl;

import com.example.lms.dto.LoginReqDto;
import com.example.lms.entity.UserEntity;
import com.example.lms.exception.CustomServiceException;
import com.example.lms.repository.UserRepository;
import com.example.lms.service.UserService;
import com.example.lms.util.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public String addUser(UserEntity userEntity) {
        try {
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            userRepository.save(userEntity);
            return "user added successfully";
        }catch(Exception e){
            throw new CustomServiceException(e.getMessage());
        }
    }

    @Override
    public String loginUser(LoginReqDto user){
        try {

            log.info("email:{} password:{}",user.getEmail(),user.getPassword());
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

            Optional<UserEntity> userOptional = userRepository.findByEmail(user.getEmail());
            if (userOptional.isPresent()) {
                log.info(userOptional.get().getName());
                String jwt = jwtUtils.generateToken(user.getEmail());
                return jwt.toString();
            } else {
                throw new RuntimeException("Invalid credentials");
            }
        } catch (Exception e) {
            throw new CustomServiceException("Unexpected error during user login");
        }
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
}
