package com.conseller.conseller.core.barter.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarterHostItemRepository extends JpaRepository<BarterHostItem, Long> {
}
