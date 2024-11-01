package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.core.barter.api.dto.request.*;
import com.conseller.conseller.core.barter.api.dto.response.*;
import com.conseller.conseller.core.barter.implement.*;
import com.conseller.conseller.core.barter.infrastructure.BarterMapper;
import com.conseller.conseller.core.barter.domain.enums.BarterStatus;
import com.conseller.conseller.core.barter.domain.enums.RequestStatus;
import com.conseller.conseller.core.barter.infrastructure.*;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterEntity;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterGuestItemEntity;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterHostItemEntity;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterRequestEntity;
import com.conseller.conseller.core.category.domain.SubCategory;
import com.conseller.conseller.core.category.implement.SubCategoryReader;
import com.conseller.conseller.core.category.infrastructure.SubCategoryEntity;
import com.conseller.conseller.core.category.infrastructure.SubCategoryRepository;
import com.conseller.conseller.core.gifticon.domain.Gifticon;
import com.conseller.conseller.core.gifticon.implement.GifticonReader;
import com.conseller.conseller.core.user.domain.User;
import com.conseller.conseller.core.user.implement.UserReader;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import com.conseller.conseller.core.gifticon.infrastructure.enums.GifticonStatus;
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

import java.util.ArrayList;
import java.util.List;
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
    private final BarterAppender barterAppender;
    private final BarterModifier barterModifier;
    private final BarterProcessor barterProcessor;
    private final BarterHostItemValidator barterHostItemValidator;

    private final BarterHostItemReader barterHostItemReader;
    private final BarterHostItemAppender barterHostItemAppender;

    private final UserReader userReader;

    private final SubCategoryReader subCategoryReader;

    private final GifticonReader gifticonReader;

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
        List<barterItemResponse> barterItemResponses = barterHostItems.stream()
                .map(
                        barterHostItem -> barterItemResponse.of(
                            barterHostItem.getHostItemName(),
                            convertString(barterHostItem.getHostItemEndDate()),
                            barterHostItem.getHostItemDataImageUrl()
                    )
                )
                .collect(Collectors.toList());
        return BarterDetailResponse.of(barter, barterItemResponses);
    }


    public Long createBarter(BarterCreateRequest barterCreateRequest) {
        User host = userReader.read(barterCreateRequest.getUserIdx());
        SubCategory preferSubCategory = subCategoryReader.read(barterCreateRequest.getPreferSubCategory());
        SubCategory maxSubCategory = subCategoryReader.read(barterProcessor.getMaxSelectedCategory(barterCreateRequest.getHostItemIdxs()));
        List<Gifticon> hostItems = gifticonReader.readAll(barterCreateRequest.getHostItemIdxs());

        barterHostItemValidator.verifyUserGifticonMatch(hostItems, barterCreateRequest.getUserIdx());
        barterHostItemValidator.isKeep(hostItems);

        Barter barter = Barter.of(barterCreateRequest, host, preferSubCategory, maxSubCategory, now());
        Long savedBarterId = barterAppender.append(barter);

        barterHostItemAppender.appendAll(barterHostItemReader.convert(hostItems, barter));

        return savedBarterId;
    }

    public void modifyBarter(Long barterIdx, BarterModifyRequest barterModifyRequest) {
        SubCategory preferSubCategory = subCategoryReader.read(barterModifyRequest.getSubCategory());
        Barter barter = barterReader.read(barterIdx);
        barterModifier.modify(barter, preferSubCategory, barterModifyRequest);
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
        UserEntity barterHost = barterEntity.getBarterHost();
        UserEntity barterRequester = barterRequestEntity.getUserEntity();

        for(BarterHostItemEntity hostItem : barterEntity.getBarterHostItemEntityList()){
            GifticonEntity gift = hostItem.getGifticonEntity();
            gift.setUserEntity(barterRequester);
            gift.setGifticonStatus(GifticonStatus.KEEP.getStatus());
            gifticonRepository.save(gift);
        }
        for(BarterGuestItemEntity guestItem : barterRequestEntity.getBarterGuestItemEntites()){
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
        List<barterItemResponse> hostGifticons = new ArrayList<>();
        List<BarterHostItemEntity> barterHostItemEntityList = barterEntity.getBarterHostItemEntityList();
        for(BarterHostItemEntity bhi : barterHostItemEntityList) {
            barterItemResponse barterItemResponse = barterItemResponse.builder()
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
            List<barterItemResponse> barterItemRespons = new ArrayList<>();
            for(BarterGuestItemEntity bgi : barterGuestItemEntityList) {
                barterItemResponse barterItemResponse = barterItemResponse.builder()
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
