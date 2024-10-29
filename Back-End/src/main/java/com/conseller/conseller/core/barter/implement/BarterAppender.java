package com.conseller.conseller.core.barter.implement;

import com.conseller.conseller.core.barter.infrastructure.BarterRepository;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BarterAppender {
    private final BarterRepository barterRepository;

    public void append(com.conseller.conseller.core.barter.domain.Barter barter) {
        barterRepository.save(BarterEntity.of(barter));
    }
}
