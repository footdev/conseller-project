package com.conseller.conseller.global.entity;

import com.conseller.conseller.core.user.infrastructure.UserEntity;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Builder
@Getter @Setter @ToString
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class BlackListEntity {

    @Id @GeneratedValue
    private Long id;

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @OneToOne
    @JoinColumn(name = "refresh_token", referencedColumnName = "refresh_token")
    private UserEntity userEntity;
}
