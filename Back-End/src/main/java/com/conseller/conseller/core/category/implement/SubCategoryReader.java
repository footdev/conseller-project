package com.conseller.conseller.core.category.implement;

import com.conseller.conseller.core.category.domain.SubCategory;
import com.conseller.conseller.core.category.infrastructure.SubCategoryRepository;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import org.springframework.stereotype.Component;

@Component
public class SubCategoryReader {
    private SubCategoryRepository subCategoryRepository;

    public SubCategory read(long subCategoryIdx) {
        return subCategoryRepository.findBySubCategoryIdx(subCategoryIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.INVALID_SUB_CATEGORY))
                .toDomain();
    }
}
