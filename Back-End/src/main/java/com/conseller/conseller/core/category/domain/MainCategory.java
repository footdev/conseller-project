package com.conseller.conseller.core.category.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @Builder
public class MainCategory {
    private long mainCategoryIdx;
    private String mainCategoryContent;
}
