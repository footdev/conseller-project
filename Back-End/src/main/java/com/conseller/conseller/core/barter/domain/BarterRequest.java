package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.core.barter.domain.enums.RequestStatus;
import com.conseller.conseller.core.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class BarterRequest {
    private Long barterRequestIdx;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private RequestStatus barterRequestStatus;
    private Barter barter;
    private User user;
    private Gifticons gifticons;
    private Boolean isDeleted;

    public void accept() {
        this.barterRequestStatus = RequestStatus.ACCEPTED;
    }

    public void reject() {
        this.barterRequestStatus = RequestStatus.REJECTED;
    }

    public void delete() {
        this.isDeleted = true;
    }

    public static BarterRequest of(Barter barter, User user, LocalDateTime createdTime) {
        return BarterRequest.builder()
                .barter(barter)
                .user(user)
                .barterRequestStatus(RequestStatus.WAIT)
                .createdTime(createdTime)
                .isDeleted(false)
                .build();
    }
}
