package com.example.lms.service.serviceImpl;

import com.example.lms.entity.UserEntity;
import com.example.lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userModelOptional = repository.findByEmail(email);
        UserEntity userEntity = userModelOptional.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return userEntity;
    }
}
