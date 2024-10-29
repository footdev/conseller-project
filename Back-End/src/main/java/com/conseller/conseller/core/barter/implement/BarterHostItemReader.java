package com.conseller.conseller.core.barter.implement;

import com.conseller.conseller.core.barter.domain.BarterHostItem;
import com.conseller.conseller.core.barter.infrastructure.BarterHostItemRepository;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BarterHostItemReader {
    private final BarterHostItemRepository barterHostItemRepository;

    public BarterHostItem read(long barterHostItemId) {
        return barterHostItemRepository.findById(barterHostItemId)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.BARTER_NO_ITEM))
                .toDomain();
    }

    public BarterHostItem readByBarterId(long barterId) {
        return barterHostItemRepository.findByBarterIdx(barterId).toDomain();
    }
}
