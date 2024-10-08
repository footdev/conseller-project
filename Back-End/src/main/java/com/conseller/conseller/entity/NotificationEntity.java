package com.conseller.conseller.entity;

import com.conseller.conseller.user.infrastructure.UserEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Builder
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = "notificationIdx")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long notificationIdx;

    @Column(name = "notification_title", nullable = false)
    private String notificationTitle;

    @Column(name = "notification_content", nullable = false)
    private String notificationContent;

    @CreatedDate
    private LocalDateTime notificationCreatedDate;

    @Column(name = "notification_type")
    private int notificationType;

    @Column(name = "seller", nullable = false)
    private boolean notificationSeller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private UserEntity notificationUserEntity;

    public static NotificationEntity from(String title, String contents, int type, boolean seller, UserEntity userEntity) {
        return NotificationEntity.builder()
                .notificationTitle(title)
                .notificationContent(contents)
                .notificationType(type)
                .notificationSeller(seller)
                .notificationUserEntity(userEntity)
                .build();
    }

    public static NotificationEntity from(String title, String contents, LocalDateTime dateTime, int type, boolean seller, UserEntity userEntity) {
        return NotificationEntity.builder()
               .notificationTitle(title)
               .notificationContent(contents)
               .notificationType(type)
                .notificationCreatedDate(dateTime)
               .notificationSeller(seller)
               .notificationUserEntity(userEntity)
               .build();
    }

    public void updateType(int type) {
        this.notificationType = type;
    }
}
