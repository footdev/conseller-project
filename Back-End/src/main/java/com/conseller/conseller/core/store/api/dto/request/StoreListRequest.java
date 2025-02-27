package com.conseller.conseller.core.store.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreListRequest {
    private Integer mainCategory;
    private Integer subCategory;
    private Integer status;
    private String searchQuery;
    private Integer page;

}
