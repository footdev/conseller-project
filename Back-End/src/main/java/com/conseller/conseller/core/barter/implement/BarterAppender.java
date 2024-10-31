package com.conseller.conseller.core.barter.implement;

import com.conseller.conseller.core.barter.infrastructure.BarterRepository;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BarterAppender {
    private final BarterRepository barterRepository;

    public Long append(com.conseller.conseller.core.barter.domain.Barter barter) {
        return barterRepository.save(BarterEntity.of(barter)).getBarterIdx();
    }
}
