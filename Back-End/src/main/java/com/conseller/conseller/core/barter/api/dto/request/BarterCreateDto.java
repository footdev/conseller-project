package com.conseller.conseller.core.barter.api.dto.request;

import com.conseller.conseller.core.barter.infrastructure.entity.BarterEntity;
import com.conseller.conseller.core.category.infrastructure.SubCategoryEntity;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class BarterCreateDto {
    private Integer mainCategory;
    private Integer subCategory;
    private String barterName;
    private String barterText;
    private String barterEndDate;
    private List<Long> selectedItemIndices;
    private Long userIdx;

    @Builder
    public BarterCreateDto(String barterName, String barterText, Integer barterSubCategory, Integer preferSubCategory, List<Long> selectedItemIndices, String barterEndDate, Long userIdx) {
        this.barterName = barterName;
        this.barterText = barterText;
        this.subCategory = barterSubCategory;
        this.mainCategory = barterSubCategory;
        this.selectedItemIndices = selectedItemIndices;
        this.barterEndDate = barterEndDate;
        this.userIdx = userIdx;
    }

    public BarterEntity toEntity(BarterCreateDto barterCreateDto, UserEntity host, LocalDateTime endDate, SubCategoryEntity subCategoryEntity, SubCategoryEntity preferCategory) {
        return BarterEntity.builder()
                .barterName(barterCreateDto.getBarterName())
                .barterText(barterCreateDto.getBarterText())
                .barterHost(host)
                .barterEndDate(endDate)
                .subCategory(subCategoryEntity)
                .preferSubCategory(preferCategory)
                .build();
    }
}

