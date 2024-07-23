package com.conseller.conseller.category.subCategory;import com.conseller.conseller.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Integer> {
    Optional<SubCategory> findBySubCategoryIdx(int subCategory);
    Optional<List<SubCategory>> findByMainCategoryMainCategoryIdx(int mainCategoryIdx);
}
