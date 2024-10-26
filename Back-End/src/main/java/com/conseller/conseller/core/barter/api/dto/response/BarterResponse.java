package com.conseller.conseller.core.barter.api.dto.response;

import com.conseller.conseller.core.barter.api.dto.request.BarterHostItemRequest;
import com.conseller.conseller.core.user.api.dto.response.UserInfoResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BarterResponse {
    private Long barterIdx;
    private String barterName;
    private String barterText;
    private String barterCreatedDate;
    private String barterEndDate;
    private String subCategory;
    private String preferSubCategory;
    private UserInfoResponse barterHost;
    private UserInfoResponse barterCompleteGuest;
    private List<BarterRequestResponse> barterRequestResponseList;
    private List<BarterHostItemRequest> barterHostItemRequestList;

    @Builder
    public BarterResponse(Long barterIdx, String barterName, String barterText, String barterCreatedDate,
                          String barterEndDate, String subCategory, String preferSubCategory,
                          UserInfoResponse barterHost, UserInfoResponse barterCompleteGuest,
                          List<BarterRequestResponse> barterRequestResponseList, List<BarterHostItemRequest> barterHostItemRequestList){
        this.barterIdx = barterIdx;
        this.barterName = barterName;
        this.barterText = barterText;
        this.barterCreatedDate = barterCreatedDate;
        this.barterEndDate = barterEndDate;
        this.subCategory = subCategory;
        this.preferSubCategory = preferSubCategory;
        this.barterHost = barterHost;
        this.barterCompleteGuest = barterCompleteGuest;
        this.barterRequestResponseList = barterRequestResponseList;
        this.barterHostItemRequestList = barterHostItemRequestList;
    }
}
