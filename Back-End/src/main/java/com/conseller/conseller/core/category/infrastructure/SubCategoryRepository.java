package com.conseller.conseller.core.category.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubCategoryRepository extends JpaRepository<SubCategoryEntity, Integer> {
    Optional<SubCategoryEntity> findBySubCategoryIdx(int subCategory);
    Optional<List<SubCategoryEntity>> findByMainCategoryMainCategoryIdx(int mainCategoryIdx);
}
