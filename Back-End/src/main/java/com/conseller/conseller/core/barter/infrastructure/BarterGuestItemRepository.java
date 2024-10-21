package com.conseller.conseller.core.barter.infrastructure;

import com.conseller.conseller.core.barter.infrastructure.entity.BarterGuestItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarterGuestItemRepository extends JpaRepository<BarterGuestItemEntity, Long> {
}
