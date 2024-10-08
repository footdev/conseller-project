package com.conseller.conseller.core.barter.api.dto.response;

import com.conseller.conseller.core.barter.api.dto.request.BarterGuestItemDto;
import com.conseller.conseller.core.user.api.dto.response.UserInfoResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@Getter
@Setter
@ToString
public class BarterRequestResponseDto {

    private Long barterRequestIdx;
    private String barterRequestStatus;
    private Long barterIdx;
    private UserInfoResponse user;
    private List<BarterGuestItemDto> barterGuestItemDtoList;

    @Builder
    public BarterRequestResponseDto(Long barterRequestIdx, String barterRequestStatus, Long barterIdx, UserInfoResponse user, List<BarterGuestItemDto> barterGuestItemDtoList){
        this.barterRequestIdx = barterRequestIdx;
        this.barterRequestStatus = barterRequestStatus;
        this.barterIdx = barterIdx;
        this.user = user;
        this.barterGuestItemDtoList = barterGuestItemDtoList;
    }
}
