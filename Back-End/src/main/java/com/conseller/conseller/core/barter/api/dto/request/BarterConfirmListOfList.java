package com.conseller.conseller.core.barter.api.dto.request;

import com.conseller.conseller.core.barter.api.dto.response.BarterItemResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BarterConfirmListOfList {
    private String buyUserImageUrl;
    private String buyUserNickName;
    private Long buyUserIdx;
    private List<BarterItemResponse> barterTradeList;

    @Builder
    public BarterConfirmListOfList(String buyUserImageUrl, String buyUserNickName, Long buyUserIdx, List<BarterItemResponse> barterTradeList){
        this.buyUserImageUrl = buyUserImageUrl;
        this.buyUserNickName = buyUserNickName;
        this.buyUserIdx = buyUserIdx;
        this.barterTradeList = barterTradeList;
    }
}
