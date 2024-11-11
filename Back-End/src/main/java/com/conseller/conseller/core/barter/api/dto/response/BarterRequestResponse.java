package com.conseller.conseller.core.barter.api.dto.response;

import com.conseller.conseller.core.barter.api.dto.request.BarterGuestItemRequest;
import com.conseller.conseller.core.user.api.dto.response.UserInfoResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Builder
@ToString
public class BarterRequestResponse {

    private Long barterRequestIdx;
    private String barterRequestStatus;
    private Long barterIdx;
    private UserInfoResponse user;
    private List<BarterGuestItemRequest> barterGuestItemRequestList;
}
