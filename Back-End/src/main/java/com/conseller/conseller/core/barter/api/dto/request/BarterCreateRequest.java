package com.conseller.conseller.core.barter.api.dto.request;

import com.conseller.conseller.core.barter.infrastructure.entity.BarterEntity;
import com.conseller.conseller.core.category.infrastructure.SubCategory;
import com.conseller.conseller.core.user.infrastructure.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class BarterCreateRequest {
    private Integer mainCategory;
    private Integer subCategory;
    private String barterName;
    private String barterText;
    private String barterEndDate;
    private List<Long> selectedItemIndices;
    private Long userIdx;

    @Builder
    public BarterCreateRequest(String barterName, String barterText, Integer barterSubCategory, Integer preferSubCategory, List<Long> selectedItemIndices, String barterEndDate, Long userIdx) {
        this.barterName = barterName;
        this.barterText = barterText;
        this.subCategory = barterSubCategory;
        this.mainCategory = barterSubCategory;
        this.selectedItemIndices = selectedItemIndices;
        this.barterEndDate = barterEndDate;
        this.userIdx = userIdx;
    }

    public BarterEntity toEntity(BarterCreateRequest barterCreateRequest, User host, LocalDateTime endDate, SubCategory subCategory, SubCategory preferCategory) {
        return BarterEntity.builder()
                .barterName(barterCreateRequest.getBarterName())
                .barterText(barterCreateRequest.getBarterText())
                .barterHost(host)
                .barterEndDate(endDate)
                .subCategory(subCategory)
                .preferSubCategory(preferCategory)
                .build();
    }
}

