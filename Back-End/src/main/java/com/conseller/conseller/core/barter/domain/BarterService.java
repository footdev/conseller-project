package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.core.barter.api.dto.request.*;
import com.conseller.conseller.core.barter.api.dto.response.*;
import com.conseller.conseller.core.barter.implement.BarterHostItemReader;
import com.conseller.conseller.core.barter.infrastructure.BarterMapper;
import com.conseller.conseller.core.barter.domain.enums.BarterStatus;
import com.conseller.conseller.core.barter.domain.enums.RequestStatus;
import com.conseller.conseller.core.barter.implement.BarterReader;
import com.conseller.conseller.core.barter.infrastructure.*;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterEntity;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterGuestItemEntity;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterHostItemEntity;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterRequestEntity;
import com.conseller.conseller.core.category.infrastructure.SubCategory;
import com.conseller.conseller.core.category.infrastructure.SubCategoryRepository;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import com.conseller.conseller.core.gifticon.infrastructure.enums.GifticonStatus;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonRepository;
import com.conseller.conseller.core.user.infrastructure.User;
import com.conseller.conseller.core.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.conseller.conseller.global.utils.DateTimeConverter.*;
import static java.time.LocalDateTime.now;

@Slf4j
@Service
@RequiredArgsConstructor
public class BarterService {

    private final BarterRepository barterRepository;
    private final BarterRepositoryImpl barterRepositoryImpl;
    private final UserRepository userRepository;
    private final GifticonRepository gifticonRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final BarterHostItemService barterHostItemService;
    private final BarterRequestRepository barterRequestRepository;
    private final BarterHostItemRepository barterHostItemRepository;
    private final BarterGuestItemRepository barterGuestItemRepository;

    private final BarterReader barterReader;
    private final BarterHostItemReader barterHostItemReader;

    //구매자 입장
    public BarterPagingResponse getBarterList(BarterFilterRequest barterFilterRequest) {
        Pageable pageable = PageRequest.of(barterFilterRequest.getPage() - 1, 10);
        Page<com.conseller.conseller.core.barter.domain.Barter> barters = barterReader.readBartersByPaging(barterFilterRequest, pageable);

        List<BarterHostItemResponse> barterHostItems = barters.stream()
                .map(barter -> barterHostItemReader.readByBarterId(barter.getBarterIdx()))
                .map(BarterHostItemResponse::of)
                .collect(Collectors.toList());

        return BarterPagingResponse.of(
                barters.getTotalPages(),
                barters.getTotalElements(),
                barterHostItems
        );
    }

    //판맨자 입장
    public BarterDetailResponse getBarter(Long barterIdx, Long userIdx) {
        BarterEntity barterEntity = barterRepository.findByBarterIdx(barterIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.BARTER_INVALID));
        List<BarterHostItemEntity> barterHostItemEntityList = barterEntity.getBarterHostItemEntityList();
        List<BarterConfirmListResponse> barterGifticonList = new ArrayList<>();
        for(BarterHostItemEntity bhi : barterHostItemEntityList) {
            GifticonEntity gifticonEntity = bhi.getGifticonEntity();
            BarterConfirmListResponse barterConfirmListResponse = BarterConfirmListResponse.builder()
                    .gifticonDataImageName(gifticonEntity.getGifticonDataImageUrl())
                    .gifticonName(gifticonEntity.getGifticonName())
                    .gifticonEndDate(convertString(gifticonEntity.getGifticonEndDate()))
                    .build();
            barterGifticonList.add(barterConfirmListResponse);
        }
        Long barterRequestIdx = (long) 0;
        List<BarterRequestEntity> barterRequestEntityList = barterEntity.getBarterRequestEntityList();
        for(BarterRequestEntity bq : barterRequestEntityList) {
            if(bq.getUser().getUserIdx() == userIdx) {
                barterRequestIdx = bq.getBarterRequestIdx();
                break;
            }
        }

        BarterDetailResponse barterDetailResponse = BarterDetailResponse.builder()
                .barterImageList(barterGifticonList)
                .preper(barterEntity.getPreferSubCategory().getSubCategoryContent())
                .barterName(barterEntity.getBarterName())
                .barterText(barterEntity.getBarterText())
                .barterUserIdx(barterEntity.getBarterHost().getUserIdx())
                .barterUserProfileUrl(barterEntity.getBarterHost().getUserProfileUrl())
                .barterUserDeposit(barterEntity.getBarterHost().getUserDeposit())
                .barterUserNickname(barterEntity.getBarterHost().getUserNickname())
                .barterRequestIdx(barterRequestIdx)
                .build();

        return barterDetailResponse;
    }


    public Long addBarter(BarterCreateRequest barterCreateRequest) {
        User user = userRepository.findByUserIdx(barterCreateRequest.getUserIdx())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        SubCategory preferSubCategory = subCategoryRepository.findById(barterCreateRequest.getSubCategory()).orElseThrow(() -> new RuntimeException("존재하지 않는 분류입니다."));
        SubCategory subCategory = null;

        LocalDateTime endDate = convertLocalDateTime(barterCreateRequest.getBarterEndDate());

        BarterEntity barterEntity = BarterMapper.INSTANCE.registBarterCreateToBarter(barterCreateRequest, user, endDate, subCategory, preferSubCategory);
        barterRepository.save(barterEntity);
        Map<Long, Integer> categoryMap = new HashMap<>();
        for(Long gifticonIdx : barterCreateRequest.getSelectedItemIndices()) {
            GifticonEntity gifticonEntity = gifticonRepository.findByGifticonIdx(gifticonIdx)
                    .orElseThrow(() -> new CustomException(CustomExceptionStatus.BARTER_NO_ITEM));
            if(categoryMap.containsKey(gifticonEntity.getSubCategory().getSubCategoryIdx())) {
                categoryMap.compute(gifticonEntity.getSubCategory().getSubCategoryIdx(), (key, k) -> k + 1);
            } else {
                categoryMap.put(gifticonEntity.getSubCategory().getSubCategoryIdx(), 1);
            }
        }
        int max_count = 0;
        long max_category = 0;
        for(long key : categoryMap.keySet() ) {
            if(categoryMap.get(key) > max_count) {
                max_count = categoryMap.get(key);
                max_category = key;
            }
        }
        SubCategory barterSubCategory = subCategoryRepository.findBySubCategoryIdx(max_category)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.SUB_CATEGORY_INVALID));
        barterEntity.setSubCategory(barterSubCategory);

        try {
            LocalDateTime gifticonEndDate = barterHostItemService.addBarterHostItem(barterCreateRequest.getSelectedItemIndices(), barterEntity);
            barterEntity.setBarterEndDate(gifticonEndDate);
            barterRepository.save(barterEntity);
        } catch(Exception e) {
            barterRepository.deleteById(barterEntity.getBarterIdx());
            throw new CustomException(CustomExceptionStatus.GIFTICON_NOT_KEEP);
        }
        return barterEntity.getBarterIdx();
    }

    @Transactional
    public void modifyBarter(Long barterIdx, BarterModifyRequest barterModifyRequest) {
        SubCategory preferSubCategory = subCategoryRepository.findBySubCategoryIdx(barterModifyRequest.getSubCategory())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.SUB_CATEGORY_INVALID));
        BarterEntity barterEntity = barterRepository.findByBarterIdx(barterIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.BARTER_INVALID));

        barterEntity.modifyBarter(barterModifyRequest, preferSubCategory);
    }

    public void deleteBarter(Long barterIdx) {
        BarterEntity barterEntity = barterRepository.findByBarterIdx(barterIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.BARTER_INVALID));
        List<BarterRequestEntity> barterRequestEntityList = barterRequestRepository.findByBarterIdx(barterIdx);

        List<BarterHostItemEntity> barterHostItemEntityList = barterEntity.getBarterHostItemEntityList();
        for(BarterHostItemEntity bhi : barterHostItemEntityList) {
            GifticonEntity gift = bhi.getGifticonEntity();
            gift.setGifticonStatus(GifticonStatus.KEEP.getStatus());
            barterHostItemRepository.deleteById(bhi.getBarterHostItemIdx());
        }

        for(BarterRequestEntity br : barterRequestEntityList) {
            if(br.getBarterRequestStatus().equals(RequestStatus.REJECTED.getStatus())) continue;

            List<BarterGuestItemEntity> barterGuestItemEntityList = br.getBarterGuestItemEntites();

            for(BarterGuestItemEntity bg : barterGuestItemEntityList) {
                GifticonEntity gifticonEntity = gifticonRepository.findById(bg.getGifticonEntity().getGifticonIdx())
                        .orElseThrow(() -> new CustomException(CustomExceptionStatus.GIFTICON_INVALID));
                gifticonEntity.setGifticonStatus(GifticonStatus.KEEP.getStatus());
                barterGuestItemRepository.deleteById(bg.getBarterGuestItemIdx());
            }
            barterRequestRepository.deleteById(br.getBarterRequestIdx());
        }

        barterRepository.deleteById(barterIdx);
    }

    public void exchangeGifticon(Long barterIdx, Long userIdx) {
        BarterEntity barterEntity = barterRepository.findByBarterIdx(barterIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.BARTER_INVALID));
        User user = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));


        Long barterRequestIdx = (long) -1;

        List<BarterRequestEntity> findBarterRequestEntityList = barterEntity.getBarterRequestEntityList();
        for(BarterRequestEntity barterRequestEntity : findBarterRequestEntityList) {
            if(barterRequestEntity.getUser().getUserIdx() == userIdx){
                barterRequestIdx = barterRequestEntity.getBarterRequestIdx();
                break;
            }
        }
        if(barterRequestIdx == -1) throw new CustomException(CustomExceptionStatus.BARTER_REQUEST_INVALID);

        BarterRequestEntity barterRequestEntity = barterRequestRepository.findByBarterRequestIdx(barterRequestIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.BARTER_REQUEST_INVALID));
        //물물교환 교환신청 리스트
        List<BarterRequestEntity> barterRequestEntityList = barterRequestRepository.findByBarterIdx(barterIdx);

        barterRequestEntity.setBarterRequestStatus(RequestStatus.ACCEPTED.getStatus());
        barterRequestRepository.save(barterRequestEntity);

        for(BarterRequestEntity br : barterRequestEntityList) {

            try{
                if(br.getBarterRequestIdx() == barterRequestIdx) continue;
            } catch (Exception e) {
                throw new RuntimeException("요청 인덱스 에러입니다.");
            }
            try {
                if(br.getBarterRequestStatus().equals(RequestStatus.REJECTED.getStatus())) continue;
            } catch (Exception e) {
                throw new RuntimeException("거절된 요청 에러입니다.");
            }

            List<BarterGuestItemEntity> barterGuestItemEntityList = br.getBarterGuestItemEntites();
            br.setBarterRequestStatus(RequestStatus.REJECTED.getStatus());
            barterRequestRepository.save(br);


            for(BarterGuestItemEntity bg : barterGuestItemEntityList) {
                GifticonEntity gifticonEntity = gifticonRepository.findById(bg.getGifticonEntity().getGifticonIdx())
                        .orElseThrow(() -> new CustomException(CustomExceptionStatus.GIFTICON_INVALID));
                gifticonEntity.setGifticonStatus(GifticonStatus.KEEP.getStatus());
                gifticonRepository.save(gifticonEntity);
            }
        }
        User barterHost = barterEntity.getBarterHost();
        User barterRequester = barterRequestEntity.getUser();

        for(BarterHostItemEntity hostItem : barterEntity.getBarterHostItemEntityList()){
            GifticonEntity gift = hostItem.getGifticonEntity();
            gift.setUser(barterRequester);
            gift.setGifticonStatus(GifticonStatus.KEEP.getStatus());
            gifticonRepository.save(gift);
        }
        for(BarterGuestItemEntity guestItem : barterRequestEntity.getBarterGuestItemEntites()){
            GifticonEntity gift = guestItem.getGifticonEntity();
            gift.setGifticonStatus(GifticonStatus.KEEP.getStatus());
            gift.setUser(barterHost);
            gifticonRepository.save(gift);
        }

        barterEntity.setBarterStatus(BarterStatus.EXCHANGED.getStatus());
        barterEntity.setBarterCompletedDate(now());
        barterEntity.setBarterCompleteGuest(user);
        barterRepository.save(barterEntity);
    }

    public void rejectRequest(Long barterIdx, Long userIdx) {
        BarterEntity barterEntity = barterRepository.findByBarterIdx(barterIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.BARTER_INVALID));

        Long barterRequestIdx = (long) -1;

        List<BarterRequestEntity> findBarterRequestEntityList = barterEntity.getBarterRequestEntityList();
        for(BarterRequestEntity barterRequestEntity : findBarterRequestEntityList) {
            if(barterRequestEntity.getUser().getUserIdx() == userIdx){
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
        List<BarterConfirmListResponse> hostGifticons = new ArrayList<>();
        List<BarterHostItemEntity> barterHostItemEntityList = barterEntity.getBarterHostItemEntityList();
        for(BarterHostItemEntity bhi : barterHostItemEntityList) {
            BarterConfirmListResponse barterConfirmListResponse = BarterConfirmListResponse.builder()
                    .gifticonDataImageName(bhi.getGifticonEntity().getGifticonDataImageUrl())
                    .gifticonName(bhi.getGifticonEntity().getGifticonName())
                    .gifticonEndDate(convertString(bhi.getGifticonEntity().getGifticonEndDate()))
                    .build();
            hostGifticons.add(barterConfirmListResponse);
        }

        List<BarterRequestEntity> barterRequestEntityList = barterEntity.getBarterRequestEntityList();
        List<BarterConfirmListOfList> barterConfirmListOfLists = new ArrayList<>();
        for(BarterRequestEntity bq : barterRequestEntityList) {
            List<BarterGuestItemEntity> barterGuestItemEntityList = bq.getBarterGuestItemEntites();
            List<BarterConfirmListResponse> barterConfirmListResponses = new ArrayList<>();
            for(BarterGuestItemEntity bgi : barterGuestItemEntityList) {
                BarterConfirmListResponse barterConfirmListResponse = BarterConfirmListResponse.builder()
                        .gifticonDataImageName(bgi.getGifticonEntity().getGifticonDataImageUrl())
                        .gifticonName(bgi.getGifticonEntity().getGifticonName())
                        .gifticonEndDate(convertString(bgi.getGifticonEntity().getGifticonEndDate()))
                        .build();
                barterConfirmListResponses.add(barterConfirmListResponse);
            }
            BarterConfirmListOfList barterConfirmListOfList = BarterConfirmListOfList.builder()
                    .buyUserImageUrl(bq.getUser().getUserProfileUrl())
                    .buyUserNickName(bq.getUser().getUserNickname())
                    .buyUserIdx(bq.getUser().getUserIdx())
                    .barterTradeList(barterConfirmListResponses)
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
