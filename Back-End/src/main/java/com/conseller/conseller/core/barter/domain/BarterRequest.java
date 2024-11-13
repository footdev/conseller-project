package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.core.barter.domain.enums.RequestStatus;
import com.conseller.conseller.core.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BarterRequest {
    private Long barterRequestIdx;
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
}
