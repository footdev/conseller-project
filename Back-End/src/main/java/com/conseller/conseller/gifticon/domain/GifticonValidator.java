package com.conseller.conseller.gifticon.domain;

import com.conseller.conseller.exception.CustomException;
import com.conseller.conseller.exception.CustomExceptionStatus;
import com.conseller.conseller.gifticon.api.dto.request.GifticonRegisterRequest;
import com.conseller.conseller.gifticon.infrastructure.GifticonRepository;
import com.conseller.conseller.gifticon.infrastructure.UsedGifticonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.conseller.conseller.utils.DateTimeConverter.*;

@Component
@RequiredArgsConstructor
public class GifticonValidator {

    private final GifticonRepository gifticonRepository;
    private final UsedGifticonRepository usedGifticonRepository;

    /*
    1. 등록된 기프티콘인가?
    2. 사용된 기프티콘인가?
    3. 유효기간이 지난 기프티콘 인가/
     */
    public void isValidGiftion(GifticonRegisterRequest request) {
        if (gifticonRepository.existsBygifticonBarcode(request.getGifticonBarcode())) {
            throw new CustomException(CustomExceptionStatus.ALREADY_REGIST_GIFTICON);
        }

        if (usedGifticonRepository.existsByUsedGifticonBarcode(request.getGifticonBarcode())) {
            throw new CustomException(CustomExceptionStatus.ALREADY_REGIST_GIFTICON);
        }

        if (convertLocalDateTime(request.getGifticonEndDate()).isBefore(LocalDateTime.now())) {
            throw new CustomException(CustomExceptionStatus.EXPIRED_GIFTICON);
        }
    }

}