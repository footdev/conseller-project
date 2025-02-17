package com.conseller.conseller.core.barter.infrastructure;

import com.conseller.conseller.core.barter.domain.BarterHostItem;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterHostItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BarterHostItemRepository extends JpaRepository<BarterHostItemEntity, Long> {

    public BarterHostItemEntity findByBarterIdx(long barterId);
    public List<BarterHostItemEntity> findAllByBarterIdx(Long barterId);
    public void deleteAllByBarterIdx(Long barterId);
}
