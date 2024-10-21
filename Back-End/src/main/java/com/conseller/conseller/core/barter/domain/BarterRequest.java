package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.core.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BarterRequest {
    private Long barterRequestIdx;
    private String barterRequestStatus;
    private Barter barter;
    private User user;
}
