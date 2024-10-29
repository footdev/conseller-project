package com.conseller.conseller.core.barter.implement;

import com.conseller.conseller.core.barter.api.dto.request.BarterFilterRequest;
import com.conseller.conseller.core.barter.domain.Barter;
import com.conseller.conseller.core.barter.infrastructure.BarterRepository;
import com.conseller.conseller.core.barter.infrastructure.BarterRepositoryImpl;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BarterReader {
    private final BarterRepository barterRepository;
    private final BarterRepositoryImpl barterRepositoryImpl;

    public Barter read(long barterIdx) {
        return barterRepository.findById(barterIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.BARTER_INVALID))
                .toDomain();
    }

    public Page<Barter> readBartersByPaging(BarterFilterRequest barterFilterRequest, Pageable pageable) {
        return barterRepositoryImpl.findBarterList(barterFilterRequest, pageable);
    }
}
