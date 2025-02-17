package com.conseller.conseller.core.barter.infrastructure;

import com.conseller.conseller.core.barter.infrastructure.entity.BarterGuestItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface BarterGuestItemRepository extends JpaRepository<BarterGuestItemEntity, Long> {
    public List<BarterGuestItemEntity> findAllByBarterRequestIdx(Long barterId);
}
