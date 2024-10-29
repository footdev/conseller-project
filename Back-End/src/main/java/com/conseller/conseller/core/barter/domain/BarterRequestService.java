package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.core.barter.api.dto.request.BarterGuestItemRequest;
import com.conseller.conseller.core.barter.infrastructure.*;
import com.conseller.conseller.core.barter.domain.enums.BarterStatus;
import com.conseller.conseller.core.barter.api.dto.request.BarterRegistRequest;
import com.conseller.conseller.core.barter.api.dto.response.BarterRequestResponse;
import com.conseller.conseller.core.barter.domain.enums.RequestStatus;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterEntity;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterGuestItemEntity;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterRequestEntity;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonRepository;
import com.conseller.conseller.core.gifticon.infrastructure.enums.GifticonStatus;
import com.conseller.conseller.core.user.infrastructure.User;
import com.conseller.conseller.core.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BarterRequestService implements BarterRequestService{

    private final BarterRequestRepository barterRequestRepository;
    private final BarterGuestItemRepository barterGuestItemRepository;
    private final BarterGuestItemService barterGuestItemService;
    private final GifticonRepository gifticonRepository;
    private final BarterRepository barterRepository;
    private final UserRepository userRepository;
    List<BarterRequestResponse> barterRequestResponseList;
    @Override
    public List<BarterRequestResponse> getBarterRequestList() {
        List<BarterRequestEntity> barterRequestEntityList = barterRequestRepository.findAll();
        List<BarterRequestResponse> barterRequestResponseList = new ArrayList<>();

        for(BarterRequestEntity barterRequestEntity : barterRequestEntityList) {
            BarterRequestResponse barterRequestResponse = barterRequestEntity.toBarterRequestResponseDto(barterRequestEntity);
            barterRequestResponseList.add(barterRequestResponse);
        }

        return barterRequestResponseList;
    }

    @Override
    public BarterRequestResponse getBarterRequest(Long barterRequestIdx) {
        BarterRequestEntity barterRequestEntity = barterRequestRepository.findByBarterRequestIdx(barterRequestIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.BARTER_INVALID));
        BarterRequestResponse barterRequestResponse = barterRequestEntity.toBarterRequestResponseDto(barterRequestEntity);

        return barterRequestResponse;
    }

    @Override
    public List<BarterRequestResponse> getBarterRequestListByBarterIdx(Long barterIdx) {
        List<BarterRequestEntity> barterRequestEntityList = barterRequestRepository.findByBarterIdx(barterIdx);
        barterRequestResponseList = new ArrayList<>();

        for(BarterRequestEntity barterRequestEntity : barterRequestEntityList) {
            BarterRequestResponse barterRequestResponse = barterRequestEntity.toBarterRequestResponseDto(barterRequestEntity);
            barterRequestResponseList.add(barterRequestResponse);
        }

        return barterRequestResponseList;
    }

    @Override
    public List<BarterRequestResponse> getBarterRequestListByRequester(Long userIdx) {
        List<BarterRequestEntity> barterRequestEntityList = barterRequestRepository.findByUserIdx(userIdx);
        barterRequestResponseList = new ArrayList<>();

        for(BarterRequestEntity barterRequestEntity : barterRequestEntityList) {
            BarterRequestResponse barterRequestResponse = barterRequestEntity.toBarterRequestResponseDto(barterRequestEntity);
            barterRequestResponseList.add(barterRequestResponse);
        }

        return barterRequestResponseList;
    }

    @Override
    public void addBarterRequest(BarterRegistRequest barterRegistRequest, Long barterIdx) {
        BarterEntity barterEntity = barterRepository.findByBarterIdx(barterIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.BARTER_INVALID));

        String statusOfBarter = barterEntity.getBarterStatus();
        if(!statusOfBarter.equals(BarterStatus.EXCHANGEABLE.getStatus()) && !statusOfBarter.equals(BarterStatus.SUGGESTED.getStatus())){
            throw new CustomException(CustomExceptionStatus.BARTER_EXPIRED_INVALID);
        }

        List<BarterRequestEntity> barterRequestEntityList = barterEntity.getBarterRequestEntityList();
        for(BarterRequestEntity bq : barterRequestEntityList) {
            if(bq.getUser().getUserIdx() == barterRegistRequest.getUserIdx()) {
                throw new CustomException(CustomExceptionStatus.BARTER_ALREADY_SEND);
            }
        }

        User user = userRepository.findByUserIdx(barterRegistRequest.getUserIdx())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        BarterRequestEntity barterRequestEntity = BarterRequestEntity.of(barterEntity, user);
        barterRequestRepository.save(barterRequestEntity);

        if(statusOfBarter.equals(BarterStatus.EXCHANGEABLE.getStatus())) {
            barterEntity.setBarterStatus(BarterStatus.SUGGESTED.getStatus());
            barterRepository.save(barterEntity);
        }

        try{
            barterGuestItemService.addBarterGuestItem(barterRegistRequest.getBarterGuestItemList(), barterRequestEntity);
        } catch(Exception e) {
            barterRequestRepository.deleteById(barterRequestEntity.getBarterRequestIdx());
            throw new CustomException(CustomExceptionStatus.GIFTICON_NOT_KEEP);
        }

    }

    @Override
    public void deleteBarterRequest(Long barterRequestIdx) {
        BarterRequestEntity barterRequestEntity = barterRequestRepository.findByBarterRequestIdx(barterRequestIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.BARTER_REQUEST_INVALID));
        List<BarterGuestItemEntity> barterGuestItemEntityList = barterRequestEntity.getBarterGuestItemEntites();

        List<BarterGuestItemRequest> barterGuestItemRequestList = new ArrayList<>();
        for(BarterGuestItemEntity bgi : barterGuestItemEntityList) {
            GifticonEntity gifticonEntity = gifticonRepository.findById(bgi.getGifticonEntity().getGifticonIdx())
                    .orElseThrow(() -> new CustomException(CustomExceptionStatus.GIFTICON_INVALID));
            gifticonEntity.setGifticonStatus(GifticonStatus.KEEP.getStatus());
            gifticonRepository.save(gifticonEntity);

            barterGuestItemRepository.deleteById(bgi.getBarterGuestItemIdx());
        }

        barterRequestRepository.deleteById(barterRequestIdx);
    }

    @Override
    public void rejectByTimeBarterRequest(Long barterRequestIdx) {
        BarterRequestEntity barterRequestEntity = barterRequestRepository.findByBarterRequestIdx(barterRequestIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.BARTER_INVALID));
        List<BarterGuestItemEntity> barterGuestItemEntityList =  barterRequestEntity.getBarterGuestItemEntites();
        for(BarterGuestItemEntity bgi : barterGuestItemEntityList) {
            GifticonEntity gifticonEntity = bgi.getGifticonEntity();
            gifticonEntity.setGifticonStatus(GifticonStatus.KEEP.getStatus());
            gifticonRepository.save(gifticonEntity);
        }
        barterRequestEntity.setBarterRequestStatus(RequestStatus.REJECTED.getStatus());
        barterRequestRepository.save(barterRequestEntity);
    }
}
