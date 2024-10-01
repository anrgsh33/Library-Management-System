package com.example.lms.service.serviceImpl;

import com.example.lms.dto.LoginResDto;
import com.example.lms.dto.RefreshTokenReqDto;
import com.example.lms.entity.RefreshTokenEntity;
import com.example.lms.entity.UserEntity;
import com.example.lms.exception.CustomServiceException;
import com.example.lms.repository.RefreshTokenRepository;
import com.example.lms.repository.UserRepository;
import com.example.lms.response.ResponseModel;
import com.example.lms.service.RefreshTokenService;
import com.example.lms.util.JWTUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Override
    public String getRefreshToken(String email){
        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .user(userRepository.findByEmail(email).get())
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(30*60*1000)) //30 minutes
                .build();
         refreshTokenRepository.save(refreshToken);
         return refreshToken.getToken();
    }

    @Transactional
    @Override
    public void deleteRefreshToken(int userId){
        try{
            log.info("token deleted started of user:"+userId);

            refreshTokenRepository.deleteByUserId(userId);
            log.info("token deleted completed of user:"+userId);

        }catch(Exception ex){
            throw new CustomServiceException("Some error occurred while deleting refresh token");
        }
    }

    @Override
    public ResponseModel<LoginResDto>refreshToken(RefreshTokenReqDto dto){
        RefreshTokenEntity refreshTokenEntity=refreshTokenRepository.findByToken(dto.getToken()).orElseThrow(()-> new CustomServiceException("Cannot find Refresh token entity"));
        RefreshTokenEntity token=this.verifyExpiration(refreshTokenEntity);

        String accessToken=jwtUtils.generateToken(refreshTokenEntity.getUser().getEmail());

        LoginResDto resDto=new LoginResDto(accessToken,refreshTokenEntity.getToken());
        return new ResponseModel("access token fetched successfully", HttpStatus.OK,resDto);

    }

    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token was expired. Please make a new signin request");
        }
        return token;
    }
}
