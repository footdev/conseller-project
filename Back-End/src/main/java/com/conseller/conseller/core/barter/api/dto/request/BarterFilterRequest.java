package com.conseller.conseller.core.barter.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BarterFilterRequest {
    private Integer mainCategory;
    private Integer subCategory;
    private Integer status;
    private String searchQuery;
    private Integer page;
}