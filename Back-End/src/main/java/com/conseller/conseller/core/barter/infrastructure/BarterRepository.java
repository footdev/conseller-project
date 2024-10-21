package com.conseller.conseller.core.barter.infrastructure;

import com.conseller.conseller.core.barter.infrastructure.entity.BarterEntity;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterHostItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface BarterRepository extends JpaRepository<BarterEntity, Long> {

    // 물물교환
    Optional<BarterEntity> findByBarterIdx(Long barterIdx);

    @Query("SELECT b FROM Barter b WHERE b.barterCompletedDate IS NULL AND b.barterHost.userIdx = ?1")
    List<BarterEntity> findByHostIdx(Long userIdx);

    @Query("SELECT b FROM Barter b WHERE b.barterCompletedDate IS NULL AND b.barterEndDate <= current_timestamp ")
    List<BarterEntity> findBarterAllExpired();
}
