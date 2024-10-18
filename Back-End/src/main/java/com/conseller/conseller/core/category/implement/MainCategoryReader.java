package com.conseller.conseller.core.category.implement;

import com.conseller.conseller.core.category.domain.MainCategory;
import com.conseller.conseller.core.category.infrastructure.MainCategoryRepository;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MainCategoryReader {
    private MainCategoryRepository mainCategoryRepository;

    public MainCategory readMainCategory(long mainCategoryIdx) {
        return mainCategoryRepository.findByMainCategoryIdx(mainCategoryIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.MAIN_CATEGORY_INVALID))
                .toDomain();
    }
}
