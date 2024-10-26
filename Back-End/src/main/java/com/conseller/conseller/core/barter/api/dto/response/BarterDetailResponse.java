package com.conseller.conseller.core.barter.api.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BarterDetailResponse {
    private List<BarterConfirmListResponse> barterImageList;
    private String preper;
    private String barterName;
    private String barterText;
    private Long barterUserIdx;
    private String barterUserProfileUrl;
    private Long barterUserDeposit;
    private String barterUserNickname;
    private Long barterRequestIdx;

    @Builder
    public BarterDetailResponse(List<BarterConfirmListResponse> barterImageList, String preper, String barterName, String barterText, Long barterUserIdx, String barterUserProfileUrl, Long barterUserDeposit, String barterUserNickname, Long barterRequestIdx) {
        this.barterImageList = barterImageList;
        this.preper = preper;
        this.barterName = barterName;
        this.barterText = barterText;
        this.barterUserIdx = barterUserIdx;
        this.barterUserProfileUrl = barterUserProfileUrl;
        this.barterUserDeposit = barterUserDeposit;
        this.barterUserNickname = barterUserNickname;
        this.barterRequestIdx = barterRequestIdx;
    }
}
