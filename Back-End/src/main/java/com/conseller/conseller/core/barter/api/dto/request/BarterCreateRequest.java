package com.conseller.conseller.core.barter.api.dto.request;

import com.conseller.conseller.core.barter.domain.Barter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public final class BarterCreateRequest {
    private Long preferSubCategory;
    private String barterName;
    private String barterText;
    private String barterEndDate;
    private List<Long> hostItemIdxs;
    private Long userIdx;

    @Builder
    public BarterCreateRequest(String barterName, String barterText, Long preferSubCategory, List<Long> hostItemIdxs, String barterEndDate, Long userIdx) {
        this.barterName = barterName;
        this.barterText = barterText;
        this.preferSubCategory = preferSubCategory;
        this.hostItemIdxs = hostItemIdxs;
        this.barterEndDate = barterEndDate;
        this.userIdx = userIdx;
    }
}

