package com.conseller.conseller.core.gifticon.implement;

import com.conseller.conseller.core.gifticon.domain.Gifticon;
import com.conseller.conseller.core.gifticon.domain.enums.GifticonStatus;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import com.conseller.conseller.core.gifticon.api.dto.request.RegisterGifticon;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonRepository;
import com.conseller.conseller.core.gifticon.infrastructure.UsedGifticonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static com.conseller.conseller.global.utils.DateTimeConverter.convertLocalDateTime;

@Component
@RequiredArgsConstructor
public class GifticonValidator {

    private final GifticonRepository gifticonRepository;
    private final UsedGifticonRepository usedGifticonRepository;

    /*
    1. 등록된 기프티콘인가?
    2. 사용된 기프티콘인가?
    3. 유효기간이 지난 기프티콘 인가?
     */
    public void isValidRegistGiftion(RegisterGifticon registerGifticon) {
        if (gifticonRepository.existsBygifticonBarcode(registerGifticon.getGifticonBarcode())) {
            throw new CustomException(CustomExceptionStatus.ALREADY_REGIST_GIFTICON);
        }

        if (usedGifticonRepository.existsByUsedGifticonBarcode(registerGifticon.getGifticonBarcode())) {
            throw new CustomException(CustomExceptionStatus.ALREADY_REGIST_GIFTICON);
        }

        if (convertLocalDateTime(registerGifticon.getGifticonEndDate()).isBefore(LocalDateTime.now())) {
            throw new CustomException(CustomExceptionStatus.ALREADY_EXPIRED_GIFTICON);
        }
    }

    public void isValidRegistGiftion(Gifticon gifticon) {
        if (gifticonRepository.existsBygifticonBarcode(gifticon.getGifticonBarcode())) {
            throw new CustomException(CustomExceptionStatus.ALREADY_REGIST_GIFTICON);
        }

        if (usedGifticonRepository.existsByUsedGifticonBarcode(gifticon.getGifticonBarcode())) {
            throw new CustomException(CustomExceptionStatus.ALREADY_REGIST_GIFTICON);
        }

        if (gifticon.getGifticonEndDate().isBefore(LocalDateTime.now())) {
            throw new CustomException(CustomExceptionStatus.ALREADY_EXPIRED_GIFTICON);
        }
    }

    public void isKeep(Gifticon gifticon) {
        if (gifticon.getGifticonStatus().equals(GifticonStatus.KEEP.getStatus())) {
            return;
        }
        throw new CustomException(CustomExceptionStatus.NOT_KEEP_GIFTICON);
    }

    public void isBarterAll(List<Gifticon> gifticons) {
        gifticons.stream()
                .filter(gifticon -> !gifticon.getGifticonStatus().equals(GifticonStatus.BARTER.getStatus()))
                .findAny()
                .ifPresent(
                        gifticon -> {
                            throw new CustomException(CustomExceptionStatus.NOT_STATUS_TRADE_GIFTICON);
                        }
                );
    }

    public void isKeepAll(List<Gifticon> gifticons) {
        gifticons.stream()
                .filter(gifticon -> !gifticon.getGifticonStatus().equals(GifticonStatus.KEEP.getStatus()))
                .findAny()
                .ifPresent(
                        gifticon -> {
                            throw new CustomException(CustomExceptionStatus.NOT_KEEP_GIFTICON);
                        }
                );
    }
}
