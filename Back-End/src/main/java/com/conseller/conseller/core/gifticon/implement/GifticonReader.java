package com.conseller.conseller.core.gifticon.implement;

import com.conseller.conseller.core.gifticon.domain.Gifticon;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonRepository;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GifticonReader {

    private GifticonRepository gifticonRepository;

    public Gifticon read(long gifticonIdx) {
        return gifticonRepository.findByGifticonIdx(gifticonIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.INVALID_GIFTICON))
                .toDomain();
    }

    public List<Gifticon> readAll(List<Long> hostItemIds) {
        return hostItemIds.stream()
                .map(this::read)
                .collect(Collectors.toList());
    }
}
