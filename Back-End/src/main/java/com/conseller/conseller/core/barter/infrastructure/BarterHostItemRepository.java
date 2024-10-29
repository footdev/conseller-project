package com.conseller.conseller.core.barter.infrastructure;

import com.conseller.conseller.core.barter.infrastructure.entity.BarterHostItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarterHostItemRepository extends JpaRepository<BarterHostItemEntity, Long> {

    BarterHostItemEntity findByBarterIdx(long barterIdx);
}
