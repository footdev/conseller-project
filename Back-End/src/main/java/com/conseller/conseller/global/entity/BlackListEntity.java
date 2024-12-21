package com.conseller.conseller.global.entity;

import com.conseller.conseller.core.user.infrastructure.UserEntity;
import com.conseller.conseller.global.security.domain.BlackList;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Builder
@Getter @EntityListeners(AuditingEntityListener.class)
public class BlackListEntity extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @Column(name = "user_idx", nullable = false)
    private Long userIdx;

    public static BlackListEntity of(String accessToken, long userIdx) {
        return BlackListEntity.builder()
                .accessToken(accessToken)
                .userIdx(userIdx)
                .build();
    }
}
