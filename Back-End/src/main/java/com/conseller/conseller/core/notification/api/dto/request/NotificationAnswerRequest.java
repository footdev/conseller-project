package com.conseller.conseller.core.notification.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationAnswerRequest {
    private Long notificationIdx;

    private Integer notificationType;

    private Boolean notificationAnswer;
}
