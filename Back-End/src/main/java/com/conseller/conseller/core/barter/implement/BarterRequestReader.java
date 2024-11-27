package com.conseller.conseller.core.barter.implement;

import com.conseller.conseller.core.barter.domain.BarterRequest;
import com.conseller.conseller.core.barter.infrastructure.BarterRequestRepository;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterRequestEntity;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BarterRequestReader {
    private final BarterRequestRepository barterRequestRepository;

    public BarterRequest read(Long id) {
        return barterRequestRepository.findByBarterRequestIdx(id)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.BARTER_REQUEST_INVALID))
                .toDomain();
    }

    public List<BarterRequest> readAll(Long barterId) {
        return barterRequestRepository.findByBarterIdx(barterId)
                .stream()
                .map(BarterRequestEntity::toDomain)
                .collect(Collectors.toList());
    }

    public List<BarterRequest> readAllByUser(Long userId) {
        return barterRequestRepository.findByUserIdx(userId)
                .stream()
                .map(BarterRequestEntity::toDomain)
                .collect(Collectors.toList());
    }
}
