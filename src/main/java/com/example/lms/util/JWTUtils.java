package com.example.lms.util;

import com.example.lms.entity.UserEntity;
import com.example.lms.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

import static java.security.KeyRep.Type.SECRET;

@Component
@Slf4j
public class JWTUtils {

    @Autowired
    private UserRepository userRepository;

    public static final String SECRET ="4A6F784C6E784F366A703477537A7A343D414E7261516336506B6F734E57504E";

    private JWTUtils() {
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public void updateUserActivity(UserDetails userDetails){
        Optional<UserEntity>userOptional=userRepository.findByEmail(userDetails.getUsername());
        if(userOptional.isPresent()){
            UserEntity user=userOptional.get();
            user.setLastActivity(java.time.LocalDateTime.now());
            userRepository.save(user);
        }
    }

    public boolean checkTokenExpiration(UserDetails userDetails){
        Optional<UserEntity>userOptional=userRepository.findByEmail(userDetails.getUsername());
        if(userOptional.isPresent()){
            UserEntity user=userOptional.get();
            LocalDateTime time1=LocalDateTime.now();
            LocalDateTime time2=user.getLastActivity();

            int diffMinutes=time1.getMinute()-time2.getMinute();

            log.info("Minutes Diff :"+diffMinutes);

            if(diffMinutes>1){
                return true;
            }


        }

        return false;

    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    public String generateToken(String userName){
        Map<String,Object> claims=new HashMap<>();
        return createToken(claims,userName);
    }

    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes=Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

