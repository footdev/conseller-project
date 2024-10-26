package com.conseller.conseller.core.barter.api.dto.response;

import com.conseller.conseller.core.barter.api.dto.request.BarterConfirmListOfList;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BarterConfirmPageResponse {
    private String barterName;
    private String barterText;
    private List<BarterConfirmListResponse> barterConfirmListResponse;
    private List<BarterConfirmListOfList> barterTradeAllList;

    @Builder
    public BarterConfirmPageResponse(String barterName, String barterText, List<BarterConfirmListResponse> barterConfirmListResponse, List<BarterConfirmListOfList> barterTradeAllList) {
        this.barterName = barterName;
        this.barterText = barterText;
        this.barterConfirmListResponse = barterConfirmListResponse;
        this.barterTradeAllList = barterTradeAllList;
    }
}
