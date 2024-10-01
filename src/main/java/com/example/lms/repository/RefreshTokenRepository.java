package com.example.lms.repository;

import com.example.lms.entity.RefreshTokenEntity;
import com.example.lms.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity,Integer> {

    @Modifying
    @Query(
            nativeQuery = true,
            value
                    = "delete from refresh_token rt where rt.user_id=:userId")
    void deleteByUserId(@Param("userId") int userId);

    Optional<RefreshTokenEntity> findByToken(String token);
}
