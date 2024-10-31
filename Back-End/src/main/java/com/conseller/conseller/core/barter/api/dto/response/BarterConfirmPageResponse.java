package com.conseller.conseller.core.barter.api.dto.response;

import com.conseller.conseller.core.barter.api.dto.request.BarterConfirmListOfList;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BarterConfirmPageResponse {
    private String barterName;
    private String barterText;
    private List<barterItemResponse> barterItemResponse;
    private List<BarterConfirmListOfList> barterTradeAllList;

    @Builder
    public BarterConfirmPageResponse(String barterName, String barterText, List<barterItemResponse> barterItemResponse, List<BarterConfirmListOfList> barterTradeAllList) {
        this.barterName = barterName;
        this.barterText = barterText;
        this.barterItemResponse = barterItemResponse;
        this.barterTradeAllList = barterTradeAllList;
    }
}
