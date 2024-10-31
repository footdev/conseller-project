package com.conseller.conseller.core.barter.implement;

import com.conseller.conseller.core.barter.domain.BarterHostItem;
import com.conseller.conseller.core.barter.infrastructure.BarterHostItemRepository;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterHostItemEntity;
import com.conseller.conseller.core.gifticon.domain.Gifticon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BarterHostItemAppender {
    private final BarterHostItemRepository barterHostItemRepository;

    public void append(BarterHostItem barterHostItem) {
        barterHostItemRepository.save(BarterHostItemEntity.of(barterHostItem));
    }

    @Transactional
    public void appendAll(List<BarterHostItem> hostItems) {
        barterHostItemRepository.saveAll(hostItems.stream()
                .map(BarterHostItemEntity::of)
                .collect(Collectors.toList()));
    }
}
