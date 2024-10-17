package com.conseller.conseller.core.gifticon.domain;

import com.conseller.conseller.core.category.infrastructure.MainCategoryEntity;
import com.conseller.conseller.core.category.infrastructure.MainCategoryRepository;
import com.conseller.conseller.core.category.infrastructure.SubCategoryEntity;
import com.conseller.conseller.core.category.infrastructure.SubCategoryRepository;
import com.conseller.conseller.core.gifticon.infrastructure.*;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import com.conseller.conseller.core.gifticon.api.dto.response.ExpiringGifticonResponse;
import com.conseller.conseller.core.gifticon.infrastructure.*;
import com.conseller.conseller.core.gifticon.api.dto.response.GifticonResponse;
import com.conseller.conseller.core.gifticon.api.dto.request.GifticonRegisterRequest;
import com.conseller.conseller.core.gifticon.domain.enums.GifticonStatus;
import com.conseller.conseller.core.notification.domain.NotificationService;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import com.conseller.conseller.core.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.conseller.conseller.global.utils.DateTimeConverter.*;

@Log4j2
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GifticonServiceImpl implements GifticonService {

    private final GifticonRepository gifticonRepository;
    private final GifticonRepositoryImpl gifticonRepositoryImpl;
    private final GifticonValidator gifticonValidator;
    private final UsedGifticonRepository usedGifticonRepository;

    private final SubCategoryRepository subCategoryRepository;
    private final MainCategoryRepository mainCategoryRepository;

    private final UserRepository userRepository;

    private final NotificationService notificationService;



    public GifticonResponse getGifticonResponse(long gifticonIdx) {
        GifticonEntity gifticonEntity = gifticonRepository.findByGifticonIdx(gifticonIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.GIFTICON_INVALID));

        return GifticonResponse.builder()
                .gifticonIdx(gifticonEntity.getGifticonIdx())
                .gifticonBarcode(gifticonEntity.getGifticonBarcode())
                .gifticonName(gifticonEntity.getGifticonName())
                .gifticonStartDate(convertString(gifticonEntity.getGifticonStartDate()))
                .gifticonEndDate(convertString(gifticonEntity.getGifticonEndDate()))
                .gifticonAllImageUrl(gifticonEntity.getGifticonAllImageUrl())
                .gifticonDataImageUrl(gifticonEntity.getGifticonDataImageUrl())
                .gifticonStatus(gifticonEntity.getGifticonStatus())
                .userIdx(gifticonEntity.getUserEntity().getUserIdx())
                .subCategoryIdx(gifticonEntity.getSubCategoryEntity().getSubCategoryIdx())
                .mainCategoryIdx(gifticonEntity.getMainCategoryEntity().getMainCategoryIdx())
                .build();
    }

    @Transactional
    @Override
    public void registGifticon(long userIdx, GifticonRegisterRequest gifticonRegisterRequest, String allImageUrl, String dataImageUrl) {

        //예외처리
        gifticonValidator.isValidGiftion(gifticonRegisterRequest);


        //카테고리 엔티티를 가져온다.
        SubCategoryEntity subCategoryEntity = subCategoryRepository.findBySubCategoryIdx(gifticonRegisterRequest.getSubCategory())
                .orElseThrow(() -> new RuntimeException("유효하지 않은 서브 카테고리 입니다."));
        MainCategoryEntity mainCategoryEntity = mainCategoryRepository.findByMainCategoryIdx(gifticonRegisterRequest.getMainCategory())
                .orElseThrow(() -> new RuntimeException("유효하지 않은 메인 카테고리 입니다."));
        UserEntity userEntity = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));


        GifticonEntity gifticonEntity = GifticonEntity.builder()
                .gifticonBarcode(gifticonRegisterRequest.getGifticonBarcode())
                .gifticonName(gifticonRegisterRequest.getGifticonName())
                .gifticonStatus(GifticonStatus.KEEP.getStatus())
                .gifticonAllImageUrl(allImageUrl)
                .gifticonDataImageUrl(dataImageUrl)
                .subCategoryEntity(subCategoryEntity)
                .mainCategoryEntity(mainCategoryEntity)
                .gifticonEndDate(convertDateTime(gifticonRegisterRequest.getGifticonEndDate()))
                .userEntity(userEntity)
                .build();

        gifticonRepository.save(gifticonEntity);

        log.info("기프티콘 등록 완료");
    }

    @Transactional
    @Override
    public void deleteGifticon(long gifticonIdx) {
        GifticonEntity gifticonEntity = gifticonRepository.findByGifticonIdx(gifticonIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ALREADY_REGIST_GIFTICON));

        //여기서 usedGifticon entity 객체 생성해서 값 넣고 save
        UsedGifticonEntity usedGifticonEntity = UsedGifticonEntity.builder()
                .usedGifticonBarcode(gifticonEntity.getGifticonBarcode())
                .usedGifticonDate(LocalDateTime.now())
                .build();

        usedGifticonRepository.save(usedGifticonEntity);

        gifticonEntity.setGifticonStatus(GifticonStatus.USED.getStatus());
    }

    public List<ExpiringGifticonResponse> getGifticonExpirationDates() {
        return gifticonRepositoryImpl.getUserIdxAndExpiringGifticonCount();
    }
}

