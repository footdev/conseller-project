package com.conseller.conseller.gifticon.domain;

import com.conseller.conseller.category.infrastructure.MainCategoryRepository;
import com.conseller.conseller.category.infrastructure.SubCategoryRepository;
import com.conseller.conseller.entity.*;
import com.conseller.conseller.exception.CustomException;
import com.conseller.conseller.exception.CustomExceptionStatus;
import com.conseller.conseller.gifticon.api.dto.response.ExpiringGifticonResponse;
import com.conseller.conseller.gifticon.infrastructure.GifticonRepository;
import com.conseller.conseller.gifticon.api.dto.response.GifticonResponse;
import com.conseller.conseller.gifticon.api.dto.request.GifticonRegisterRequest;
import com.conseller.conseller.gifticon.domain.enums.GifticonStatus;
import com.conseller.conseller.gifticon.infrastructure.GifticonRepositoryImpl;
import com.conseller.conseller.gifticon.infrastructure.UsedGifticonRepository;
import com.conseller.conseller.notification.domain.NotificationService;
import com.conseller.conseller.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.conseller.conseller.utils.DateTimeConverter.*;

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
        Gifticon gifticon = gifticonRepository.findByGifticonIdx(gifticonIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.GIFTICON_INVALID));

        return GifticonResponse.builder()
                .gifticonIdx(gifticon.getGifticonIdx())
                .gifticonBarcode(gifticon.getGifticonBarcode())
                .gifticonName(gifticon.getGifticonName())
                .gifticonStartDate(convertString(gifticon.getGifticonStartDate()))
                .gifticonEndDate(convertString(gifticon.getGifticonEndDate()))
                .gifticonAllImageUrl(gifticon.getGifticonAllImageUrl())
                .gifticonDataImageUrl(gifticon.getGifticonDataImageUrl())
                .gifticonStatus(gifticon.getGifticonStatus())
                .userIdx(gifticon.getUser().getUserIdx())
                .subCategoryIdx(gifticon.getSubCategory().getSubCategoryIdx())
                .mainCategoryIdx(gifticon.getMainCategory().getMainCategoryIdx())
                .build();
    }

    @Transactional
    @Override
    public void registGifticon(long userIdx, GifticonRegisterRequest gifticonRegisterRequest, String allImageUrl, String dataImageUrl) {

        //예외처리
        gifticonValidator.isValidGiftion(gifticonRegisterRequest);


        //카테고리 엔티티를 가져온다.
        SubCategory subCategory = subCategoryRepository.findBySubCategoryIdx(gifticonRegisterRequest.getSubCategory())
                .orElseThrow(() -> new RuntimeException("유효하지 않은 서브 카테고리 입니다."));
        MainCategory mainCategory = mainCategoryRepository.findByMainCategoryIdx(gifticonRegisterRequest.getMainCategory())
                .orElseThrow(() -> new RuntimeException("유효하지 않은 메인 카테고리 입니다."));
        User user = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));


        Gifticon gifticon = Gifticon.builder()
                .gifticonBarcode(gifticonRegisterRequest.getGifticonBarcode())
                .gifticonName(gifticonRegisterRequest.getGifticonName())
                .gifticonStatus(GifticonStatus.KEEP.getStatus())
                .gifticonAllImageUrl(allImageUrl)
                .gifticonDataImageUrl(dataImageUrl)
                .subCategory(subCategory)
                .mainCategory(mainCategory)
                .gifticonEndDate(convertDateTime(gifticonRegisterRequest.getGifticonEndDate()))
                .user(user)
                .build();

        gifticonRepository.save(gifticon);

        log.info("기프티콘 등록 완료");
    }

    @Transactional
    @Override
    public void deleteGifticon(long gifticonIdx) {
        Gifticon gifticon = gifticonRepository.findByGifticonIdx(gifticonIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ALREADY_REGIST_GIFTICON));

        //여기서 usedGifticon entity 객체 생성해서 값 넣고 save
        UsedGifticon usedGifticon = UsedGifticon.builder()
                .usedGifticonBarcode(gifticon.getGifticonBarcode())
                .usedGifticonDate(LocalDateTime.now())
                .build();

        usedGifticonRepository.save(usedGifticon);

        gifticon.setGifticonStatus(GifticonStatus.USED.getStatus());
    }

    public List<ExpiringGifticonResponse> getGifticonExpirationDates() {
        return gifticonRepositoryImpl.getUserIdxAndExpiringGifticonCount();
    }
}
