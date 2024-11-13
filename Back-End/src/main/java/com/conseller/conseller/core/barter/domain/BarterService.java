package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.core.barter.api.dto.request.*;
import com.conseller.conseller.core.barter.api.dto.response.*;
import com.conseller.conseller.core.barter.implement.*;
import com.conseller.conseller.core.barter.domain.enums.RequestStatus;
import com.conseller.conseller.core.category.domain.SubCategory;
import com.conseller.conseller.core.category.implement.SubCategoryReader;
import com.conseller.conseller.core.gifticon.domain.Gifticon;
import com.conseller.conseller.core.gifticon.implement.GifticonModifier;
import com.conseller.conseller.core.gifticon.implement.GifticonReader;
import com.conseller.conseller.core.gifticon.implement.GifticonValidator;
import com.conseller.conseller.core.user.domain.User;
import com.conseller.conseller.core.user.implement.UserReader;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import com.conseller.conseller.core.gifticon.domain.enums.GifticonStatus;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.conseller.conseller.global.utils.DateTimeConverter.*;
import static java.time.LocalDateTime.now;

@Slf4j
@Service
@RequiredArgsConstructor
public class BarterService {

    private final BarterReader barterReader;
    private final BarterAppender barterAppender;
    private final BarterModifier barterModifier;
    private final BarterRemover barterRemover;
    private final BarterProcessor barterProcessor;

    private final BarterHostItemReader barterHostItemReader;
    private final BarterHostItemAppender barterHostItemAppender;
    private final BarterHostItemValidator barterHostItemValidator;
    private final BarterHostItemRemover barterHostItemRemover;

    private final BarterRequestReader barterRequestReader;
    private final BarterRequestRemover barterRequestRemover;
    private final BarterRequestManager barterRequestManager;

    private final BarterGuestItemReader barterGuestItemReader;

    private final UserReader userReader;

    private final SubCategoryReader subCategoryReader;

    private final GifticonReader gifticonReader;
    private final GifticonModifier gifticonModifier;
    private final GifticonValidator gifticonValidator;

    public BarterPagingResponse getBarters(BarterFilterRequest barterFilterRequest) {
        Pageable pageable = PageRequest.of(barterFilterRequest.getPage() - 1, 10);
        Page<Barter> barters = barterReader.readBartersByPaging(barterFilterRequest, pageable);
        return BarterPagingResponse.of(
                barters.getTotalPages(),
                barters.getTotalElements(),
                barters.stream()
                        .map(barter -> barterHostItemReader.readByBarterId(barter.getBarterIdx()))
                        .map(BarterHostItemResponse::of)
                        .collect(Collectors.toList())
        );
    }

    public BarterDetailResponse getBarter(Long barterIdx, Long userIdx) {
        Barter barter = barterReader.read(barterIdx);
        List<BarterHostItem> barterHostItems = barterHostItemReader.readAll(barterIdx);
        return BarterDetailResponse.of(barter, BarterItemResponse.of(barterHostItems));
    }


    @Transactional
    public Long createBarter(BarterCreateRequest barterCreateRequest) {
        User host = userReader.read(barterCreateRequest.getUserIdx());
        SubCategory preferSubCategory = subCategoryReader.read(barterCreateRequest.getPreferSubCategory());
        SubCategory maxSubCategory = subCategoryReader.read(barterProcessor.getMaxSelectedCategory(barterCreateRequest.getHostItemIdxs()));
        List<Gifticon> hostItems = gifticonReader.readAll(barterCreateRequest.getHostItemIdxs());

        barterHostItemValidator.verifyUserGifticonMatch(hostItems, barterCreateRequest.getUserIdx());
        barterHostItemValidator.isKeep(hostItems);

        Barter barter = Barter.of(barterCreateRequest, host, preferSubCategory, maxSubCategory, now());
        Long savedBarterId = barterAppender.append(barter);

        barterHostItemAppender.appendAll(BarterHostGifticons.from(barterHostItemReader.convert(hostItems, barter)));

        return savedBarterId;
    }

    @Transactional
    public void modifyBarter(Long barterIdx, BarterModifyRequest barterModifyRequest) {
        SubCategory preferSubCategory = subCategoryReader.read(barterModifyRequest.getSubCategory());
        Barter barter = barterReader.read(barterIdx);
        barterModifier.modify(barter, preferSubCategory, barterModifyRequest);
    }

    @Transactional
    public void deleteBarter(Long barterId) {
        barterRemover.remove(barterId);
        barterRequestRemover.removeAll(barterId);
    }

    @Transactional
    public void exchangeGifticon(Long barterId, Long userId, Long barterRequestId) {
        User host = userReader.read(userId);
        Barter barter = barterReader.read(barterId);

        BarterRequest acceptedRequest = barterRequestReader.read(barterRequestId);
        List<BarterRequest> barterRequests = barterRequestReader.readAll(barterId);

        Gifticons hostGifticons = BarterHostGifticons.from(barterHostItemReader.readAll(barter.getBarterIdx()));
        Gifticons guestGifticons = BarterGuestGifticons.from(barterGuestItemReader.readAll(barterRequestId));

        gifticonValidator.isBarterAll(hostGifticons.getGifticons());
        gifticonValidator.isBarterAll(guestGifticons.getGifticons());

        barterRequestManager.acceptAt(acceptedRequest);
        barterRequestManager.rejectOtherRequests(barterRequests, acceptedRequest.getBarterRequestIdx());
        barterProcessor.exchangeGifticons(hostGifticons, guestGifticons, host, acceptedRequest.getUser());
        barterModifier.accept(barter, acceptedRequest.getUser());
    }

    public void rejectRequest(Long barterIdx, Long userIdx) {
        BarterEntity barterEntity = barterRepository.findByBarterIdx(barterIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.BARTER_INVALID));

        Long barterRequestIdx = (long) -1;

        List<BarterRequestEntity> findBarterRequestEntityList = barterEntity.getBarterRequestEntityList();
        for(BarterRequestEntity barterRequestEntity : findBarterRequestEntityList) {
            if(barterRequestEntity.getUserEntity().getUserIdx() == userIdx){
                barterRequestIdx = barterRequestEntity.getBarterRequestIdx();
                break;
            }
        }
        if(barterRequestIdx == -1) throw new CustomException(CustomExceptionStatus.BARTER_REQUEST_INVALID);
        BarterRequestEntity barterRequestEntity = barterRequestRepository.findByBarterRequestIdx(barterRequestIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.BARTER_REQUEST_INVALID));

        barterRequestEntity.setBarterRequestStatus(RequestStatus.REJECTED.getStatus());
        barterRequestRepository.save(barterRequestEntity);

        List<BarterGuestItemEntity> gifticonList = barterRequestEntity.getBarterGuestItemEntites();
        for(BarterGuestItemEntity bgi : gifticonList) {
            GifticonEntity gifticonEntity = bgi.getGifticonEntity();
            gifticonEntity.setGifticonStatus(GifticonStatus.KEEP.getStatus());
            gifticonRepository.save(gifticonEntity);
        }
    }

    public BarterConfirmPageResponse getBarterConfirmPage(Long barterIdx) {

        //barter 정보들
        BarterEntity barterEntity = barterRepository.findByBarterIdx(barterIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.BARTER_INVALID));

        //barter의 기프티콘 리스트
        List<BarterItemResponse> hostGifticons = new ArrayList<>();
        List<BarterHostItemEntity> barterHostItemEntityList = barterEntity.getBarterHostItemEntityList();
        for(BarterHostItemEntity bhi : barterHostItemEntityList) {
            BarterItemResponse barterItemResponse = barterItemResponse.builder()
                    .gifticonDataImageName(bhi.getGifticonEntity().getGifticonDataImageUrl())
                    .gifticonName(bhi.getGifticonEntity().getGifticonName())
                    .gifticonEndDate(convertString(bhi.getGifticonEntity().getGifticonEndDate()))
                    .build();
            hostGifticons.add(barterItemResponse);
        }

        List<BarterRequestEntity> barterRequestEntityList = barterEntity.getBarterRequestEntityList();
        List<BarterConfirmListOfList> barterConfirmListOfLists = new ArrayList<>();
        for(BarterRequestEntity bq : barterRequestEntityList) {
            List<BarterGuestItemEntity> barterGuestItemEntityList = bq.getBarterGuestItemEntites();
            List<BarterItemResponse> barterItemRespons = new ArrayList<>();
            for(BarterGuestItemEntity bgi : barterGuestItemEntityList) {
                BarterItemResponse barterItemResponse = barterItemResponse.builder()
                        .gifticonDataImageName(bgi.getGifticonEntity().getGifticonDataImageUrl())
                        .gifticonName(bgi.getGifticonEntity().getGifticonName())
                        .gifticonEndDate(convertString(bgi.getGifticonEntity().getGifticonEndDate()))
                        .build();
                barterItemRespons.add(barterItemResponse);
            }
            BarterConfirmListOfList barterConfirmListOfList = BarterConfirmListOfList.builder()
                    .buyUserImageUrl(bq.getUserEntity().getUserProfileUrl())
                    .buyUserNickName(bq.getUserEntity().getUserNickname())
                    .buyUserIdx(bq.getUserEntity().getUserIdx())
                    .barterTradeList(barterItemRespons)
                    .build();
            barterConfirmListOfLists.add(barterConfirmListOfList);
        }


        return BarterConfirmPageResponse.builder()
                .barterName(barterEntity.getBarterName())
                .barterText(barterEntity.getBarterText())
                .barterConfirmList(hostGifticons)
                .barterTradeAllList(barterConfirmListOfLists)
                .build();
    }

    public List<BarterEntity> getExpiredBarterList() {
        return barterRepository.findBarterAllExpired();
    }

    public BarterHostItemResponse getPopularBarter() {
        Long popularBarterIdx = (long) 0;
        Integer barterRequestCount = 0;

        List<BarterEntity> barterEntityList = barterRepository.findAll();
        for(BarterEntity barterEntity : barterEntityList) {
            if(barterEntity.getBarterRequestEntityList().size() > barterRequestCount) {
                barterRequestCount = barterEntity.getBarterRequestEntityList().size();
                popularBarterIdx = barterEntity.getBarterIdx();
            }
        }

        BarterEntity barterEntity = barterRepository.findByBarterIdx(popularBarterIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.BARTER_INVALID));



        return BarterMapper.INSTANCE.toBarterItemData(barterEntity);
    }
}
