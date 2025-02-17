package com.conseller.conseller.core.category.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MainCategoryRepository extends JpaRepository<MainCategoryEntity, Integer> {
    Optional<MainCategoryEntity> findByMainCategoryIdx(long id);
}
