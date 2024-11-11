package com.conseller.conseller.core.barter.implement;

import com.conseller.conseller.core.barter.domain.BarterGuestItem;
import com.conseller.conseller.core.barter.infrastructure.BarterGuestItemRepository;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterGuestItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BarterGuestItemReader {
    private final BarterGuestItemRepository barterGuestItemRepository;

    public List<BarterGuestItem> readAll(Long barterRequestId) {
        return barterGuestItemRepository.findAllByBarterRequestIdx(barterRequestId)
                .stream()
                .map(BarterGuestItemEntity::toDomain)
                .collect(Collectors.toList());
    }
}
