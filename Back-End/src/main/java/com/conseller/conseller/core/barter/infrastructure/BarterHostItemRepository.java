package com.conseller.conseller.core.barter.infrastructure;

import com.conseller.conseller.core.barter.infrastructure.entity.BarterHostItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BarterHostItemRepository extends JpaRepository<BarterHostItemEntity, Long> {

    List<BarterHostItemEntity> findByBarterIdx(long barterIdx);
}
