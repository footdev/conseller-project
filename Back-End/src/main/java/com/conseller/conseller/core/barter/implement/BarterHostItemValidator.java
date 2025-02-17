package com.conseller.conseller.core.barter.implement;

import com.conseller.conseller.core.gifticon.domain.Gifticon;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonRepository;
import com.conseller.conseller.core.gifticon.domain.enums.GifticonStatus;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BarterHostItemValidator {

    private final GifticonRepository gifticonRepository;

    public void verifyUserGifticonMatch(List<Gifticon> hostItems, Long userId) {
        hostItems.stream()
                .filter(gifticon -> gifticon.getGifticonIdx() != userId)
                .findAny()
                .ifPresent(gifticon -> {
                    throw new CustomException(CustomExceptionStatus.BARTER_NO_ITEM);
                });
    }

    public void isKeep(List<Gifticon> hostItems) {
        hostItems.stream()
                .filter(gifticon -> !gifticon.getGifticonStatus().equals(GifticonStatus.KEEP.getStatus()))
                .findAny()
                .ifPresent(gifticon -> {
                    throw new CustomException(CustomExceptionStatus.NOT_KEEP_GIFTICON);
                });
    }
}
