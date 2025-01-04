package com.conseller.conseller.core.category.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategoryEntity, Integer> {
    Optional<SubCategoryEntity> findBySubCategoryIdx(long id);
    Optional<List<SubCategoryEntity>> findByMainCategoryMainCategoryIdx(long mainCategoryId);
}
