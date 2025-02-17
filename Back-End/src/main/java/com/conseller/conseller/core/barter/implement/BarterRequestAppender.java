package com.conseller.conseller.core.barter.implement;

import com.conseller.conseller.core.barter.domain.BarterRequest;
import com.conseller.conseller.core.barter.infrastructure.BarterRequestRepository;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterRequestEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BarterRequestAppender {
    private final BarterRequestRepository barterRequestRepository;

    public void append(BarterRequest barterRequest) {
        barterRequestRepository.save(BarterRequestEntity.of(barterRequest));
    }

    public void appendAll(List<BarterRequest> barterRequests) {
        barterRequestRepository.saveAll(BarterRequestEntity.of(barterRequests));
    }
}
