package com.conseller.conseller.core.barter.api.dto.response;

import com.conseller.conseller.core.barter.domain.Barter;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BarterDetailResponse {
    private List<BarterItemResponse> barterImageList;
    private String preper;
    private String barterName;
    private String barterText;
    private Long barterUserIdx;
    private String barterUserProfileUrl;
    private Long barterUserDeposit;
    private String barterUserNickname;

    @Builder
    public BarterDetailResponse(List<BarterItemResponse> barterImageList, String preper, String barterName, String barterText, Long barterUserIdx, String barterUserProfileUrl, Long barterUserDeposit, String barterUserNickname) {
        this.barterImageList = barterImageList;
        this.preper = preper;
        this.barterName = barterName;
        this.barterText = barterText;
        this.barterUserIdx = barterUserIdx;
        this.barterUserProfileUrl = barterUserProfileUrl;
        this.barterUserDeposit = barterUserDeposit;
        this.barterUserNickname = barterUserNickname;
    }

    public static BarterDetailResponse of(Barter barter, List<BarterItemResponse> barterImageList) {
        return BarterDetailResponse.builder()
                .barterImageList(barterImageList)
                .preper(barter.getPreferSubCategoryContent())
                .barterName(barter.getBarterName())
                .barterText(barter.getBarterText())
                .barterUserIdx(barter.getHostIdx())
                .barterUserProfileUrl(barter.getHostProfileUrl())
                .barterUserDeposit(barter.getHostDeposit())
                .barterUserNickname(barter.getHostNickname())
                .build();
    }
}
