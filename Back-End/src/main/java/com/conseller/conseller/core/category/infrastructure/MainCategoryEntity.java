package com.conseller.conseller.core.category.infrastructure;

import com.conseller.conseller.core.category.domain.MainCategory;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "mainCategoryIdx")

public class MainCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mainCategoryIdx;

    @Column(name = "main_category_content", nullable = false)
    private String mainCategoryContent;

    public static MainCategoryEntity of(MainCategory mainCategory) {
        return MainCategoryEntity.builder()
                .mainCategoryIdx(mainCategory.getMainCategoryIdx())
                .mainCategoryContent(mainCategory.getMainCategoryContent())
                .build();
    }

    public MainCategory toDomain() {
        return MainCategory.builder()
                .mainCategoryIdx(mainCategoryIdx)
                .mainCategoryContent(mainCategoryContent)
                .build();
    }
}
