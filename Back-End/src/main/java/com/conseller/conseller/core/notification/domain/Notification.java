package com.conseller.conseller.core.notification.domain;

import com.conseller.conseller.core.user.infrastructure.UserEntity;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class Notification {
    private long notificationIdx;
    private String notificationTitle;
    private String notificationContent;
    private LocalDateTime notificationCreatedDate;
    private int notificationType;
    private boolean notificationSeller;
    private UserEntity notificationUserEntity;
}
