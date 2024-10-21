package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.core.barter.api.dto.request.BarterGuestItemDto;
import com.conseller.conseller.core.barter.infrastructure.*;
import com.conseller.conseller.core.barter.domain.enums.BarterStatus;
import com.conseller.conseller.core.barter.api.dto.request.BarterRequestRegistDto;
import com.conseller.conseller.core.barter.api.dto.response.BarterRequestResponseDto;
import com.conseller.conseller.core.barter.domain.enums.RequestStatus;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterEntity;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterGuestItemEntity;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterRequestEntity;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonRepository;
import com.conseller.conseller.core.gifticon.infrastructure.enums.GifticonStatus;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import com.conseller.conseller.core.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BarterRequestServiceImpl implements BarterRequestService{

    private final BarterRequestRepository barterRequestRepository;
    private final BarterGuestItemRepository barterGuestItemRepository;
    private final BarterGuestItemService barterGuestItemService;
    private final GifticonRepository gifticonRepository;
    private final BarterRepository barterRepository;
    private final UserRepository userRepository;
    List<BarterRequestResponseDto> barterRequestResponseDtoList;
    @Override
    public List<BarterRequestResponseDto> getBarterRequestList() {
        List<BarterRequestEntity> barterRequestEntityList = barterRequestRepository.findAll();
        List<BarterRequestResponseDto> barterRequestResponseDtoList = new ArrayList<>();

        for(BarterRequestEntity barterRequestEntity : barterRequestEntityList) {
            BarterRequestResponseDto barterRequestResponseDto = barterRequestEntity.toBarterRequestResponseDto(barterRequestEntity);
            barterRequestResponseDtoList.add(barterRequestResponseDto);
        }

        return barterRequestResponseDtoList;
    }

    @Override
    public BarterRequestResponseDto getBarterRequest(Long barterRequestIdx) {
        BarterRequestEntity barterRequestEntity = barterRequestRepository.findByBarterRequestIdx(barterRequestIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.BARTER_INVALID));
        BarterRequestResponseDto barterRequestResponseDto = barterRequestEntity.toBarterRequestResponseDto(barterRequestEntity);

        return barterRequestResponseDto;
    }

    @Override
    public List<BarterRequestResponseDto> getBarterRequestListByBarterIdx(Long barterIdx) {
        List<BarterRequestEntity> barterRequestEntityList = barterRequestRepository.findByBarterIdx(barterIdx);
        barterRequestResponseDtoList = new ArrayList<>();

        for(BarterRequestEntity barterRequestEntity : barterRequestEntityList) {
            BarterRequestResponseDto barterRequestResponseDto = barterRequestEntity.toBarterRequestResponseDto(barterRequestEntity);
            barterRequestResponseDtoList.add(barterRequestResponseDto);
        }

        return barterRequestResponseDtoList;
    }

    @Override
    public List<BarterRequestResponseDto> getBarterRequestListByRequester(Long userIdx) {
        List<BarterRequestEntity> barterRequestEntityList = barterRequestRepository.findByUserIdx(userIdx);
        barterRequestResponseDtoList = new ArrayList<>();

        for(BarterRequestEntity barterRequestEntity : barterRequestEntityList) {
            BarterRequestResponseDto barterRequestResponseDto = barterRequestEntity.toBarterRequestResponseDto(barterRequestEntity);
            barterRequestResponseDtoList.add(barterRequestResponseDto);
        }

        return barterRequestResponseDtoList;
    }

    @Override
    public void addBarterRequest(BarterRequestRegistDto barterRequestRegistDto, Long barterIdx) {
        BarterEntity barterEntity = barterRepository.findByBarterIdx(barterIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.BARTER_INVALID));

        String statusOfBarter = barterEntity.getBarterStatus();
        if(!statusOfBarter.equals(BarterStatus.EXCHANGEABLE.getStatus()) && !statusOfBarter.equals(BarterStatus.SUGGESTED.getStatus())){
            throw new CustomException(CustomExceptionStatus.BARTER_EXPIRED_INVALID);
        }

        List<BarterRequestEntity> barterRequestEntityList = barterEntity.getBarterRequestEntityList();
        for(BarterRequestEntity bq : barterRequestEntityList) {
            if(bq.getUserEntity().getUserIdx() == barterRequestRegistDto.getUserIdx()) {
                throw new CustomException(CustomExceptionStatus.BARTER_ALREADY_SEND);
            }
        }

        UserEntity userEntity = userRepository.findByUserIdx(barterRequestRegistDto.getUserIdx())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        BarterRequestEntity barterRequestEntity = BarterRequestEntity.of(barterEntity, userEntity);
        barterRequestRepository.save(barterRequestEntity);

        if(statusOfBarter.equals(BarterStatus.EXCHANGEABLE.getStatus())) {
            barterEntity.setBarterStatus(BarterStatus.SUGGESTED.getStatus());
            barterRepository.save(barterEntity);
        }

        try{
            barterGuestItemService.addBarterGuestItem(barterRequestRegistDto.getBarterGuestItemList(), barterRequestEntity);
        } catch(Exception e) {
            barterRequestRepository.deleteById(barterRequestEntity.getBarterRequestIdx());
            throw new CustomException(CustomExceptionStatus.GIFTICON_NOT_KEEP);
        }

    }

    @Override
    public void deleteBarterRequest(Long barterRequestIdx) {
        BarterRequestEntity barterRequestEntity = barterRequestRepository.findByBarterRequestIdx(barterRequestIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.BARTER_REQUEST_INVALID));
        List<BarterGuestItemEntity> barterGuestItemEntityList = barterRequestEntity.getBarterGuestItemEntityList();

        List<BarterGuestItemDto> barterGuestItemDtoList = new ArrayList<>();
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
        List<BarterGuestItemEntity> barterGuestItemEntityList =  barterRequestEntity.getBarterGuestItemEntityList();
        for(BarterGuestItemEntity bgi : barterGuestItemEntityList) {
            GifticonEntity gifticonEntity = bgi.getGifticonEntity();
            gifticonEntity.setGifticonStatus(GifticonStatus.KEEP.getStatus());
            gifticonRepository.save(gifticonEntity);
        }
        barterRequestEntity.setBarterRequestStatus(RequestStatus.REJECTED.getStatus());
        barterRequestRepository.save(barterRequestEntity);
    }
}
