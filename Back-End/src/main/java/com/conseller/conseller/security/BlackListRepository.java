package com.conseller.conseller.security;

import com.conseller.conseller.entity.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {
    boolean existsByAccessToken(String token);
    boolean existsByUser_RefreshToken(String token);
}
