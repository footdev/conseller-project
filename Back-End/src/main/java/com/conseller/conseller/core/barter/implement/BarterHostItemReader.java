package com.conseller.conseller.core.barter.implement;

import com.conseller.conseller.core.barter.domain.Barter;
import com.conseller.conseller.core.barter.domain.BarterHostItem;
import com.conseller.conseller.core.barter.infrastructure.BarterHostItemRepository;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterHostItemEntity;
import com.conseller.conseller.core.gifticon.domain.Gifticon;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BarterHostItemReader {
    private final BarterHostItemRepository barterHostItemRepository;

    public BarterHostItem read(long barterHostItemId) {
        return barterHostItemRepository.findById(barterHostItemId)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.BARTER_NO_ITEM))
                .toDomain();
    }

    public List<BarterHostItem> readAll(Long barterId) {
        return barterHostItemRepository.findAllByBarterIdx(barterId).stream()
                .map(BarterHostItemEntity::toDomain)
                .collect(Collectors.toList());
    }

    public BarterHostItem readByBarterId(long barterId) {
        return barterHostItemRepository.findByBarterIdx(barterId).toDomain();
    }

    public List<BarterHostItem> convert(List<Gifticon> hostItems, Barter barter) {
        return  hostItems.stream()
                .map(hostItem -> BarterHostItem.from(hostItem, barter))
                .collect(Collectors.toList());
    }
}
