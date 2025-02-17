package com.conseller.conseller.core.barter.api.dto.response;

import com.conseller.conseller.core.barter.api.dto.request.BarterGuestItemRequest;
import com.conseller.conseller.core.barter.domain.BarterRequest;
import com.conseller.conseller.core.barter.domain.Gifticons;
import com.conseller.conseller.core.gifticon.api.dto.response.GifticonResponse;
import com.conseller.conseller.core.user.api.dto.response.UserInfoResponse;
import com.conseller.conseller.core.user.domain.User;
import com.conseller.conseller.global.utils.DateTimeConverter;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Builder
public class BarterRequestResponse {

    private Long barterRequestIdx;
    private String barterRequestStatus;
    private Long guestIdx;
    private String createdTime;
    private String guestNickName;
    private String guestImageUrl;
    private List<GifticonResponse> gifticons;

    public static BarterRequestResponse of(BarterRequest barterRequest, Gifticons gifticons) {
        return BarterRequestResponse.builder()
                .barterRequestIdx(barterRequest.getBarterRequestIdx())
                .barterRequestStatus(barterRequest.getBarterRequestStatus().getStatus())
                .guestIdx(barterRequest.getUser().getUserIdx())
                .createdTime(DateTimeConverter.convertString(barterRequest.getCreatedTime()))
                .guestNickName(barterRequest.getUser().getUserNickname())
                .guestImageUrl(barterRequest.getUser().getUserProfileUrl())
                .gifticons(GifticonResponse.ofAll(gifticons))
                .build();
    }
}
