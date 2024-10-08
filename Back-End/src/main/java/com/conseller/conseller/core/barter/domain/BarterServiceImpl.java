package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.barter.infrastructure.*;
import com.conseller.conseller.barter.api.dto.request.*;
import com.conseller.conseller.core.barter.api.dto.request.*;
import com.conseller.conseller.core.barter.api.dto.response.BarterConfirmList;
import com.conseller.conseller.core.barter.api.dto.response.BarterDetailResponseDTO;
import com.conseller.conseller.core.barter.api.dto.response.BarterItemData;
import com.conseller.conseller.core.barter.api.dto.response.BarterResponse;
import com.conseller.conseller.core.barter.api.dto.mapper.BarterMapper;
import com.conseller.conseller.core.barter.domain.enums.BarterStatus;
import com.conseller.conseller.core.barter.domain.enums.RequestStatus;
import com.conseller.conseller.core.barter.infrastructure.*;
import com.conseller.conseller.core.category.infrastructure.SubCategoryEntity;
import com.conseller.conseller.core.category.infrastructure.SubCategoryRepository;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import com.conseller.conseller.core.gifticon.domain.enums.GifticonStatus;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonRepository;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
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

import static java.time.LocalDateTime.now;

@Slf4j
@Service
@RequiredArgsConstructor
public class BarterServiceImpl implements BarterService{

    private final BarterRepository barterRepository;
    private final BarterRepositoryImpl barterRepositoryImpl;
    private final UserRepository userRepository;
    private final GifticonRepository gifticonRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final BarterHostItemService barterHostItemService;
    private final BarterRequestRepository barterRequestRepository;
    private final BarterHostItemRepository barterHostItemRepository;
    private final BarterGuestItemRepository barterGuestItemRepository;


    //구매자 입장
    @Override
    public BarterResponse getBarterList(BarterFilterDto barterFilterDto) {
        Pageable pageable = PageRequest.of(barterFilterDto.getPage() - 1, 10);

        Page<BarterEntity> barterPage = barterRepositoryImpl.findBarterList(barterFilterDto, pageable);

        List<BarterItemData> barterItemDataList= new ArrayList<>();

        for(BarterEntity barterEntity : barterPage) {
            BarterItemData barterItemData = BarterMapper.INSTANCE.toBarterItemData(barterEntity);
            barterItemDataList.add(barterItemData);
        }

        BarterResponse response = new BarterResponse(
                barterPage.getTotalPages(),
                barterPage.getTotalElements(),
                barterItemDataList);

        return response;
    }

    //판맨자 입장
    @Override
    public BarterDetailResponseDTO getBarter(Long barterIdx, Long userIdx) {
        BarterEntity barterEntity = barterRepository.findByBarterIdx(barterIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.BARTER_INVALID));
        List<BarterHostItem> barterHostItemList = barterEntity.getBarterHostItemList();
        List<BarterConfirmList> barterGifticonList = new ArrayList<>();
        for(BarterHostItem bhi : barterHostItemList) {
            GifticonEntity gifticonEntity = bhi.getGifticonEntity();
            BarterConfirmList barterConfirmList = BarterConfirmList.builder()
                    .gifticonDataImageName(gifticonEntity.getGifticonDataImageUrl())
                    .gifticonName(gifticonEntity.getGifticonName())
                    .gifticonEndDate(convertString(gifticonEntity.getGifticonEndDate()))
                    .build();
            barterGifticonList.add(barterConfirmList);
        }
        Long barterRequestIdx = (long) 0;
        List<BarterRequestEntity> barterRequestEntityList = barterEntity.getBarterRequestEntityList();
        for(BarterRequestEntity bq : barterRequestEntityList) {
            if(bq.getUserEntity().getUserIdx() == userIdx) {
                barterRequestIdx = bq.getBarterRequestIdx();
                break;
            }
        }

        BarterDetailResponseDTO barterDetailResponseDTO = BarterDetailResponseDTO.builder()
                .barterImageList(barterGifticonList)
                .preper(barterEntity.getPreferSubCategoryEntity().getSubCategoryContent())
                .barterName(barterEntity.getBarterName())
                .barterText(barterEntity.getBarterText())
                .barterUserIdx(barterEntity.getBarterHost().getUserIdx())
                .barterUserProfileUrl(barterEntity.getBarterHost().getUserProfileUrl())
                .barterUserDeposit(barterEntity.getBarterHost().getUserDeposit())
                .barterUserNickname(barterEntity.getBarterHost().getUserNickname())
                .barterRequestIdx(barterRequestIdx)
                .build();

        return barterDetailResponseDTO;
    }


    @Override
    public Long addBarter(BarterCreateDto barterCreateDto) {
        UserEntity userEntity = userRepository.findByUserIdx(barterCreateDto.getUserIdx())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        SubCategoryEntity preferSubCategoryEntity = subCategoryRepository.findById(barterCreateDto.getSubCategory()).orElseThrow(() -> new RuntimeException("존재하지 않는 분류입니다."));
        SubCategoryEntity subCategoryEntity = null;

        LocalDateTime endDate = convertLocalDateTime(barterCreateDto.getBarterEndDate());

        BarterEntity barterEntity = BarterMapper.INSTANCE.registBarterCreateToBarter(barterCreateDto, userEntity, endDate, subCategoryEntity, preferSubCategoryEntity);
        barterRepository.save(barterEntity);
        Map<Integer, Integer> categoryMap = new HashMap<>();
        for(Long gifticonIdx : barterCreateDto.getSelectedItemIndices()) {
            GifticonEntity gifticonEntity = gifticonRepository.findByGifticonIdx(gifticonIdx)
                    .orElseThrow(() -> new CustomException(CustomExceptionStatus.BARTER_NO_ITEM));
            if(categoryMap.containsKey(gifticonEntity.getSubCategoryEntity().getSubCategoryIdx())) {
                int k = categoryMap.get(gifticonEntity.getSubCategoryEntity().getSubCategoryIdx());
                categoryMap.put(gifticonEntity.getSubCategoryEntity().getSubCategoryIdx(), k+1);
            } else {
                categoryMap.put(gifticonEntity.getSubCategoryEntity().getSubCategoryIdx(), 1);
            }
        }
        int max_count = 0;
        int max_category = 0;
        for(Integer key : categoryMap.keySet() ) {
            if(categoryMap.get(key) > max_count) {
                max_count = categoryMap.get(key);
                max_category = key;
            }
        }
        SubCategoryEntity barterSubCategoryEntity = subCategoryRepository.findBySubCategoryIdx(max_category)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.SUB_CATEGORY_INVALID));
        barterEntity.setSubCategoryEntity(barterSubCategoryEntity);

        try {
            LocalDateTime gifticonEndDate = barterHostItemService.addBarterHostItem(barterCreateDto.getSelectedItemIndices(), barterEntity);
            barterEntity.setBarterEndDate(gifticonEndDate);
            barterRepository.save(barterEntity);
        } catch(Exception e) {
            barterRepository.deleteById(barterEntity.getBarterIdx());
            throw new CustomException(CustomExceptionStatus.GIFTICON_NOT_KEEP);
        }
        return barterEntity.getBarterIdx();
    }

    @Override
    @Transactional
    public void modifyBarter(Long barterIdx, BarterModifyRequestDto barterModifyRequestDto) {
        SubCategoryEntity preferSubCategoryEntity = subCategoryRepository.findBySubCategoryIdx(barterModifyRequestDto.getSubCategory())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.SUB_CATEGORY_INVALID));
        BarterEntity barterEntity = barterRepository.findByBarterIdx(barterIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.BARTER_INVALID));

        barterEntity.modifyBarter(barterModifyRequestDto, preferSubCategoryEntity);
    }

    @Override
    public void deleteBarter(Long barterIdx) {
        BarterEntity barterEntity = barterRepository.findByBarterIdx(barterIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.BARTER_INVALID));
        List<BarterRequestEntity> barterRequestEntityList = barterRequestRepository.findByBarterIdx(barterIdx);

        List<BarterHostItem> barterHostItemList = barterEntity.getBarterHostItemList();
        for(BarterHostItem bhi : barterHostItemList) {
            GifticonEntity gift = bhi.getGifticonEntity();
            gift.setGifticonStatus(GifticonStatus.KEEP.getStatus());
            barterHostItemRepository.deleteById(bhi.getBarterHostItemIdx());
        }

        for(BarterRequestEntity br : barterRequestEntityList) {
            if(br.getBarterRequestStatus().equals(RequestStatus.REJECTED.getStatus())) continue;

            List<BarterGuestItem> barterGuestItemList = br.getBarterGuestItemList();

            for(BarterGuestItem bg : barterGuestItemList) {
                GifticonEntity gifticonEntity = gifticonRepository.findById(bg.getGifticonEntity().getGifticonIdx())
                        .orElseThrow(() -> new CustomException(CustomExceptionStatus.GIFTICON_INVALID));
                gifticonEntity.setGifticonStatus(GifticonStatus.KEEP.getStatus());
                barterGuestItemRepository.deleteById(bg.getBarterGuestItemIdx());
            }
            barterRequestRepository.deleteById(br.getBarterRequestIdx());
        }

        barterRepository.deleteById(barterIdx);
    }

    @Override
    public void exchangeGifticon(Long barterIdx, Long userIdx) {
        BarterEntity barterEntity = barterRepository.findByBarterIdx(barterIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.BARTER_INVALID));
        UserEntity userEntity = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));


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

            List<BarterGuestItem> barterGuestItemList = br.getBarterGuestItemList();
            br.setBarterRequestStatus(RequestStatus.REJECTED.getStatus());
            barterRequestRepository.save(br);


            for(BarterGuestItem bg : barterGuestItemList) {
                GifticonEntity gifticonEntity = gifticonRepository.findById(bg.getGifticonEntity().getGifticonIdx())
                        .orElseThrow(() -> new CustomException(CustomExceptionStatus.GIFTICON_INVALID));
                gifticonEntity.setGifticonStatus(GifticonStatus.KEEP.getStatus());
                gifticonRepository.save(gifticonEntity);
            }
        }
        UserEntity barterHost = barterEntity.getBarterHost();
        UserEntity barterRequester = barterRequestEntity.getUserEntity();

        for(BarterHostItem hostItem : barterEntity.getBarterHostItemList()){
            GifticonEntity gift = hostItem.getGifticonEntity();
            gift.setUserEntity(barterRequester);
            gift.setGifticonStatus(GifticonStatus.KEEP.getStatus());
            gifticonRepository.save(gift);
        }
        for(BarterGuestItem guestItem : barterRequestEntity.getBarterGuestItemList()){
            GifticonEntity gift = guestItem.getGifticonEntity();
            gift.setGifticonStatus(GifticonStatus.KEEP.getStatus());
            gift.setUserEntity(barterHost);
            gifticonRepository.save(gift);
        }

        barterEntity.setBarterStatus(BarterStatus.EXCHANGED.getStatus());
        barterEntity.setBarterCompletedDate(now());
        barterEntity.setBarterCompleteGuest(userEntity);
        barterRepository.save(barterEntity);
    }

    @Override
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

        List<BarterGuestItem> gifticonList = barterRequestEntity.getBarterGuestItemList();
        for(BarterGuestItem bgi : gifticonList) {
            GifticonEntity gifticonEntity = bgi.getGifticonEntity();
            gifticonEntity.setGifticonStatus(GifticonStatus.KEEP.getStatus());
            gifticonRepository.save(gifticonEntity);
        }
    }

    @Override
    public BarterConfirmPageResponseDTO getBarterConfirmPage(Long barterIdx) {

        //barter 정보들
        BarterEntity barterEntity = barterRepository.findByBarterIdx(barterIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.BARTER_INVALID));

        //barter의 기프티콘 리스트
        List<BarterConfirmList> hostGifticons = new ArrayList<>();
        List<BarterHostItem> barterHostItemList = barterEntity.getBarterHostItemList();
        for(BarterHostItem bhi : barterHostItemList) {
            BarterConfirmList barterConfirmList = BarterConfirmList.builder()
                    .gifticonDataImageName(bhi.getGifticonEntity().getGifticonDataImageUrl())
                    .gifticonName(bhi.getGifticonEntity().getGifticonName())
                    .gifticonEndDate(convertString(bhi.getGifticonEntity().getGifticonEndDate()))
                    .build();
            hostGifticons.add(barterConfirmList);
        }

        List<BarterRequestEntity> barterRequestEntityList = barterEntity.getBarterRequestEntityList();
        List<BarterConfirmListOfList> barterConfirmListOfLists = new ArrayList<>();
        for(BarterRequestEntity bq : barterRequestEntityList) {
            List<BarterGuestItem> barterGuestItemList = bq.getBarterGuestItemList();
            List<BarterConfirmList> barterConfirmLists = new ArrayList<>();
            for(BarterGuestItem bgi : barterGuestItemList) {
                BarterConfirmList barterConfirmList = BarterConfirmList.builder()
                        .gifticonDataImageName(bgi.getGifticonEntity().getGifticonDataImageUrl())
                        .gifticonName(bgi.getGifticonEntity().getGifticonName())
                        .gifticonEndDate(convertString(bgi.getGifticonEntity().getGifticonEndDate()))
                        .build();
                barterConfirmLists.add(barterConfirmList);
            }
            BarterConfirmListOfList barterConfirmListOfList = BarterConfirmListOfList.builder()
                    .buyUserImageUrl(bq.getUserEntity().getUserProfileUrl())
                    .buyUserNickName(bq.getUserEntity().getUserNickname())
                    .buyUserIdx(bq.getUserEntity().getUserIdx())
                    .barterTradeList(barterConfirmLists)
                    .build();
            barterConfirmListOfLists.add(barterConfirmListOfList);
        }


        return BarterConfirmPageResponseDTO.builder()
                .barterName(barterEntity.getBarterName())
                .barterText(barterEntity.getBarterText())
                .barterConfirmList(hostGifticons)
                .barterTradeAllList(barterConfirmListOfLists)
                .build();
    }

    @Override
    public List<BarterEntity> getExpiredBarterList() {
        return barterRepository.findBarterAllExpired();
    }

    @Override
    public BarterItemData getPopularBarter() {
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
