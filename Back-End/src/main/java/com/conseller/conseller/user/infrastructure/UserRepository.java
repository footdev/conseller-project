package com.conseller.conseller.user.infrastructure;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserId(String id);

    Optional<UserEntity> findByUserName(String name);

    Optional<UserEntity> findByUserIdx(Long userIdx);

    Optional<UserEntity> findByUserEmailAndUserName(String userEmail, String userName);

    Optional<UserEntity> findByUserEmailAndUserId(String userEmail, String userId);

    @Query("select u.refreshToken from User u where u.userId = :userId")
    Optional<String> findRefreshTokenByUserId(@Param("userId") String userId);

    boolean existsByUserIdx(Long userIdx);

    boolean existsByUserId(String userId);

    boolean existsByUserPassword(String userPassword);

    boolean existsByUserNickname(String userNickname);

    boolean existsByUserEmail(String userEmail);

    boolean existsByUserPhoneNumber(String userPhoneNumber);

    boolean existsByUserIdxAndUserPassword(long userIdx, String userPassword);
}
