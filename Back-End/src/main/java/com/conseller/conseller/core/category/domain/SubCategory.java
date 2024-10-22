package com.conseller.conseller.core.category.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @Builder
public class SubCategory {
    private long subCategoryIdx;
    private String subCategoryContent;
    private MainCategory mainCategory;
}
