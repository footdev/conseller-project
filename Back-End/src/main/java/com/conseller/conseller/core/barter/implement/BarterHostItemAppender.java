package com.conseller.conseller.core.barter.implement;

import com.conseller.conseller.core.barter.domain.BarterHostGifticons;
import com.conseller.conseller.core.barter.domain.BarterHostItem;
import com.conseller.conseller.core.barter.infrastructure.BarterHostItemRepository;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterHostItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class BarterHostItemAppender {

    private final BarterHostItemRepository barterHostItemRepository;

    public void append(BarterHostItem barterHostItem) {
        barterHostItemRepository.save(BarterHostItemEntity.of(barterHostItem));
    }

    @Transactional
    public void appendAll(BarterHostGifticons hostItems) {
        barterHostItemRepository.saveAll(BarterHostItemEntity.of(hostItems.getHostItems()));
    }
}
