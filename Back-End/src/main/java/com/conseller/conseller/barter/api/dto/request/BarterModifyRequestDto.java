package com.conseller.conseller.barter.api.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BarterModifyRequestDto {
    private Integer mainCategoryIdx;
    private Integer subCategory;
    private String barterName;
    private String barterText;
    private String barterEndDate;
}