package com.conseller.conseller.core.store.domain;

import com.conseller.conseller.core.store.api.dto.response.*;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.store.infrastructure.StoreEntity;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import com.conseller.conseller.core.gifticon.infrastructure.enums.GifticonStatus;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonRepository;
import com.conseller.conseller.core.store.api.dto.mapper.StoreMapper;
import com.conseller.conseller.core.store.api.dto.request.ModifyStoreRequest;
import com.conseller.conseller.core.store.api.dto.request.RegistStoreRequest;
import com.conseller.conseller.core.store.api.dto.request.StoreListRequest;
import com.conseller.conseller.core.store.domain.enums.StoreStatus;
import com.conseller.conseller.core.store.infrastructure.StoreRepository;
import com.conseller.conseller.core.store.infrastructure.StoreRepositoryImpl;
import com.conseller.conseller.core.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final GifticonRepository gifticonRepository;
    private final StoreRepositoryImpl storeRepositoryImpl;

    // 스토어 목록
    @Transactional(readOnly = true)
    public StoreListResponse getStoreList(StoreListRequest request) { //queryDSL 사용
        Pageable pageable = PageRequest.of(request.getPage() - 1, 10);

        Page<StoreEntity> stores = storeRepositoryImpl.findStoreList(request, pageable);

        List<StoreItemData> storeItemDataList = StoreMapper.INSTANCE.storesToItemDatas(stores.getContent());

        StoreListResponse response = new StoreListResponse(storeItemDataList,
                stores.getTotalElements(),
                stores.getTotalPages());

        return response;
    }

    // 스토어 글 등록
    public Long registStore(RegistStoreRequest request) {

        UserEntity userEntity = userRepository.findById(request.getUserIdx())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));
        GifticonEntity gifticonEntity = gifticonRepository.findById(request.getGifticonIdx())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.GIFTICON_INVALID));

        if(!gifticonEntity.getGifticonStatus().equals(GifticonStatus.KEEP.getStatus())){
            throw new CustomException(CustomExceptionStatus.GIFTICON_NOT_KEEP);
        }else {
            StoreEntity storeEntity = StoreMapper.INSTANCE.registStoreRequestToStore(request, userEntity, gifticonEntity);

            storeEntity.setStoreEndDate(gifticonEntity.getGifticonEndDate());

            gifticonEntity.setGifticonStatus(GifticonStatus.STORE.getStatus());

            StoreEntity saveStoreEntity = storeRepository.save(storeEntity);

            return saveStoreEntity.getStoreIdx();
        }
    }

    // 스토어 글 상세보기
    @Transactional(readOnly = true)
    public DetailStoreResponse detailStore(Long storeIdx) {
        StoreEntity storeEntity = storeRepository.findById(storeIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.STORE_INVALID));

        DetailStoreResponse response = StoreMapper.INSTANCE.entityToDetailStoreResponse(storeEntity);

        return response;
    }

    //스토어 글 수정
    public void modifyStore(Long storeIdx , ModifyStoreRequest storeRequest) {
        StoreEntity storeEntity = storeRepository.findById(storeIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.STORE_INVALID));

        if(storeEntity.getStoreStatus().equals(StoreStatus.IN_PROGRESS.getStatus())){
            storeEntity.setStorePrice(storeRequest.getStorePrice());
            storeEntity.setStoreText(storeRequest.getStoreText());
        }
        else {
            throw new CustomException(CustomExceptionStatus.ALREADY_TRADE_STORE);
        }
    }

    // 스토어 글 삭제
    public void deleteStore(Long storeIdx) {
        StoreEntity storeEntity = storeRepository.findById(storeIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.STORE_INVALID));
        GifticonEntity gifticonEntity = gifticonRepository.findById(storeEntity.getGifticonEntity().getGifticonIdx())
                        .orElseThrow(() -> new CustomException(CustomExceptionStatus.GIFTICON_INVALID));

        gifticonEntity.setGifticonStatus(GifticonStatus.KEEP.getStatus());

        storeRepository.deleteById(storeIdx);
    }

    // 스토어 거래 진행
    @Override
    public StoreTradeResponse tradeStore(Long storeIdx, Long consumerIdx) {
        StoreEntity storeEntity = storeRepository.findById(storeIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.STORE_INVALID));
        UserEntity consumer = userRepository.findById(consumerIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        StoreTradeResponse response = null;

        if(storeEntity.getStoreStatus().equals(StoreStatus.IN_PROGRESS.getStatus())){
            // 구매자의 인덱스를 저장
            storeEntity.setConsumer(consumer);

            // 거래 상태 거래중으로 변경
            storeEntity.setStoreStatus(StoreStatus.IN_TRADE.getStatus());

            response = new StoreTradeResponse(storeEntity.getUserEntity().getUsername() , storeEntity.getUserEntity().getUserAccount(),
                    storeEntity.getUserEntity().getUserAccountBank());
        }
        else {
            throw new CustomException(CustomExceptionStatus.ALREADY_TRADE_STORE);
        }

        return response;
    }

    // 스토어 거래 취소
    @Override
    public void cancelStore(Long storeIdx) {
        StoreEntity storeEntity = storeRepository.findById(storeIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.STORE_INVALID));

        // 거래 상태를 진행중으로 변경
        storeEntity.setStoreStatus(StoreStatus.IN_PROGRESS.getStatus());

        // 구매자 정보 삭제
        storeEntity.setConsumer(null);
    }

    // 스토어 거래 확정
    @Override
    public void confirmStore(Long storeIdx) {
        StoreEntity storeEntity = storeRepository.findById(storeIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.STORE_INVALID));
        GifticonEntity gifticonEntity = gifticonRepository.findById(storeEntity.getGifticonEntity().getGifticonIdx())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.GIFTICON_INVALID));
        UserEntity userEntity = userRepository.findById(storeEntity.getConsumer().getUserIdx())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        storeEntity.setStoreStatus(StoreStatus.AWARDED.getStatus());

        gifticonEntity.setUserEntity(userEntity);
        gifticonEntity.setGifticonStatus(GifticonStatus.KEEP.getStatus());
    }

    // 스토어 판매자 입금확인
    @Override
    public StoreConfirmResponse getConfirmStore(Long storeIdx) {
        StoreEntity storeEntity = storeRepository.findById(storeIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.STORE_INVALID));

        StoreConfirmResponse response = StoreMapper.INSTANCE.storeToComfirm(storeEntity);

        return response;
    }

    @Override
    public List<StoreEntity> getStoreConfirmList() {
        List<StoreEntity> storeEntities = storeRepository.findByStoreListConfirm();

        return storeEntities;
    }

    @Override
    public List<StoreEntity> getStoreExpiredList() {
        return storeRepository.findStoreAllExpired();
    }

    @Override
    public void rejectStore(Long storeIdx) {
        StoreEntity storeEntity = storeRepository.findById(storeIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.STORE_INVALID));
        GifticonEntity gifticonEntity = gifticonRepository.findById(storeEntity.getGifticonEntity().getGifticonIdx())
                        .orElseThrow(() -> new CustomException(CustomExceptionStatus.GIFTICON_INVALID));

        storeEntity.setStoreStatus(StoreStatus.EXPIRED.getStatus());
        gifticonEntity.setGifticonStatus(GifticonStatus.KEEP.getStatus());
    }

    @Override
    public List<Integer> getMainCategory() {
        List<StoreEntity> storeEntities = storeRepository.findAwardedStoreList();

        int[] mainCategoryCount = new int[6];

        for(StoreEntity storeEntity : storeEntities) {
            int idx = (int) storeEntity.getGifticonEntity().getMainCategoryEntity().getMainCategoryIdx();
            mainCategoryCount[idx]++;
        }

        int maxIdx = 1;
        for(int i = 1; i < 6; i++) {
            if(mainCategoryCount[i] > mainCategoryCount[maxIdx]){
                maxIdx = i;
            }
        }

        int secondIdx = 2;
        for(int i = 1; i < 6; i++) {
            if(maxIdx != i && mainCategoryCount[i] > mainCategoryCount[maxIdx]){
                secondIdx = i;
            }
        }

        List<Integer> list = new ArrayList<>();
        list.add(maxIdx);
        list.add(secondIdx);

        return list;
    }

    @Override
    public List<Integer> getSubCategory() {
        List<StoreEntity> storeEntities = storeRepository.findAwardedStoreList();

        int[] subCategoryCount = new int[14];

        for(StoreEntity storeEntity : storeEntities) {
            int idx = (int) storeEntity.getGifticonEntity().getSubCategoryEntity().getSubCategoryIdx();
            subCategoryCount[idx]++;
        }

        int maxIdx = 1;
        for(int i = 1; i < 14; i++) {
            if(subCategoryCount[i] > subCategoryCount[maxIdx]){
                maxIdx = i;
            }
        }

        int secondIdx = 2;
        for(int i = 1; i < 14; i++) {
            if(maxIdx != i && subCategoryCount[i] > subCategoryCount[maxIdx]){
                secondIdx = i;
            }
        }

        List<Integer> list = new ArrayList<>();
        list.add(maxIdx);
        list.add(secondIdx);

        return list;
    }

}
