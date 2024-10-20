package com.conseller.conseller.core.category.infrastructure;

import com.conseller.conseller.core.category.domain.SubCategory;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "subCategoryIdx")
public class SubCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long subCategoryIdx;

    @Column(name = "sub_category_content", nullable = false)
    private String subCategoryContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_category_idx")
    private MainCategoryEntity mainCategoryEntity;

    public SubCategory toDomain() {
        return SubCategory.builder()
                .subCategoryIdx(subCategoryIdx)
                .subCategoryContent(subCategoryContent)
                .mainCategory(mainCategoryEntity.toDomain())
                .build();
    }

    public static SubCategoryEntity of(SubCategory subCategory) {
        return SubCategoryEntity.builder()
                .subCategoryIdx(subCategory.getSubCategoryIdx())
                .subCategoryContent(subCategory.getSubCategoryContent())
                .mainCategoryEntity(MainCategoryEntity.of(subCategory.getMainCategory()))
                .build();
    }
}
