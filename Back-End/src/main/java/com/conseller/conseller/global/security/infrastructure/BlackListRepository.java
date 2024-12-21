package com.conseller.conseller.global.security.infrastructure;

import com.conseller.conseller.global.entity.BlackListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListRepository extends JpaRepository<BlackListEntity, Long> {
    boolean existsByAccessToken(String token);
    boolean existsByUser_RefreshToken(String token);
}
