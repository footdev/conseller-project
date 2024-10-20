package com.conseller.conseller.core.auction.domain;

import com.conseller.conseller.core.auction.api.dto.mapper.AuctionMapper;
import com.conseller.conseller.core.auction.api.dto.request.AuctionListRequest;
import com.conseller.conseller.core.auction.api.dto.request.ModifyAuctionRequest;
import com.conseller.conseller.core.auction.api.dto.request.RegistAuctionRequest;
import com.conseller.conseller.auction.api.dto.response.*;
import com.conseller.conseller.core.auction.api.dto.response.*;
import com.conseller.conseller.core.auction.domain.enums.AuctionStatus;
import com.conseller.conseller.core.auction.infrastructure.AuctionEntity;
import com.conseller.conseller.core.auction.infrastructure.AuctionRepository;
import com.conseller.conseller.core.auction.infrastructure.AuctionRepositoryImpl;
import com.conseller.conseller.core.bid.infrastructure.AuctionBidRepository;
import com.conseller.conseller.core.bid.api.dto.mapper.AuctionBidMapper;
import com.conseller.conseller.core.bid.domain.enums.BidStatus;
import com.conseller.conseller.core.bid.infrastructure.AuctionBid;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import com.conseller.conseller.core.gifticon.infrastructure.enums.GifticonStatus;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonRepository;
import com.conseller.conseller.core.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuctionServiceImpl implements AuctionService{
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final GifticonRepository gifticonRepository;
    private final AuctionBidRepository auctionBidRepository;
    private final AuctionRepositoryImpl auctionImplRepository;

    // 경매 목록
    @Override
    @Transactional(readOnly = true)
    public AuctionListResponse getAuctionList(long auctionId, AuctionListRequest request) {
        AuctionEntity cursor = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_INVALID));

        List<AuctionItemData> auctionItemDataList = AuctionMapper.INSTANCE.auctionsToItemDatas(auctionImplRepository.findAuctionListByCursor(cursor, request));

        return new AuctionListResponse(auctionItemDataList);
    }

    // 경매 글 등록
    @Override
    public Long registAuction(RegistAuctionRequest request) {
        UserEntity userEntity = userRepository.findById(request.getUserIdx())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));
        GifticonEntity gifticonEntity = gifticonRepository.findById(request.getGifticonIdx())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.GIFTICON_INVALID));

        if(!gifticonEntity.getGifticonStatus().equals(GifticonStatus.KEEP.getStatus())){
            throw new CustomException(CustomExceptionStatus.GIFTICON_NOT_KEEP);
        }else {
            AuctionEntity auctionEntity = AuctionMapper.INSTANCE.registAuctionRequestToAuction(request, userEntity, gifticonEntity);
            auctionEntity.setAuctionEndDate(gifticonEntity.getGifticonEndDate());
            gifticonEntity.setGifticonStatus(GifticonStatus.AUCTION.getStatus());
            AuctionEntity saveAuctionEntity = auctionRepository.save(auctionEntity);
            return saveAuctionEntity.getAuctionIdx();
        }
    }

    // 경매 글 상세보기
    @Override
    @Transactional(readOnly = true)
    public DetailAuctionResponse detailAuction(Long auctionIdx) {
        AuctionEntity auctionEntity = auctionRepository.findById(auctionIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_INVALID));

        List<AuctionBidItemData> auctionBidItemDataList = AuctionMapper.INSTANCE.bidsToItemDatas(auctionEntity.getAuctionBidList());

        return AuctionMapper.INSTANCE.entityToDetailAuctionResponse(auctionEntity, auctionBidItemDataList);
    }

    // 경매 글 수정
    @Override
    public void modifyAuction(Long auctionIdx, ModifyAuctionRequest auctionRequest) {
        AuctionEntity auctionEntity = auctionRepository.findById(auctionIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_INVALID));

        auctionEntity.setAuctionText(auctionRequest.getAuctionText());
    }

    // 경매 글 삭제
    @Override
    public void deleteAuction(Long auctionIdx) {
        AuctionEntity auctionEntity = auctionRepository.findById(auctionIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_INVALID));
        GifticonEntity gifticonEntity = gifticonRepository.findById(auctionEntity.getGifticonEntity().getGifticonIdx())
                        .orElseThrow(() -> new CustomException(CustomExceptionStatus.GIFTICON_INVALID));

        gifticonEntity.setGifticonStatus(GifticonStatus.KEEP.getStatus());


        auctionRepository.deleteById(auctionIdx);

    }

    // 경매 거래 진행
    @Override
    public AuctionTradeResponse tradeAuction(Long auctionIdx, Long userIdx) {
        AuctionEntity auctionEntity = auctionRepository.findById(auctionIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_INVALID));
        UserEntity userEntity = userRepository.findById(userIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        AuctionTradeResponse response = null;

        if(auctionEntity.getAuctionStatus().equals(AuctionStatus.IN_PROGRESS.getStatus())) {
            if (!auctionEntity.getUserEntity().getUserIdx().equals(userIdx)) { //즉시 구매자 라면
                log.info("immediate");


                boolean isExist = false;
                Long bidIdx = 0L;

                // 이미 입찰이 있고 그 입찰이 지금 경매라면
//                for (AuctionBid bid : auction.getAuctionBidList()) {
//                    if (bid.getUser().getUserIdx().equals(userIdx)) {
//                        isExist = true;
//                        bidIdx = bid.getAuctionBidIdx();
//                    }
//                }

                if (isExist) {
                    AuctionBid auctionBid = auctionBidRepository.findById(bidIdx)
                            .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_BID_INVALID));

                    // 입찰 정보 수정
                    auctionBid.setAuctionBidPrice(auctionEntity.getUpperPrice());
                    auctionBid.setAuctionEntity(auctionEntity);
                } else { // 없으면
                    AuctionBid auctionBid = AuctionBidMapper.INSTANCE.registImToAuctionBid(userEntity, auctionEntity, auctionEntity.getUpperPrice());

                    // 새로 등록
                    auctionBidRepository.save(auctionBid);
                }
            }

            // 경매 상태 거래중으로 변경
            auctionEntity.setAuctionStatus(AuctionStatus.IN_TRADE.getStatus());

            //입찰 상태를 낙찰 예정으로 변경
//            AuctionBid bid = auctionBidRepository.findByUser_UserIdxAndAuction_AuctionIdx(auction.getHighestBidUser().getUserIdx(), auctionIdx)
//                    .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_BID_INVALID));

//            bid.setAuctionBidStatus(BidStatus.EXPECTED.getStatus());

            // 판매자의 계좌번호와 은행 전달
            response = new AuctionTradeResponse(auctionEntity.getUserEntity().getUsername() , auctionEntity.getUserEntity().getUserAccount(),
                    auctionEntity.getUserEntity().getUserAccountBank());
        }
        else {
            throw new CustomException(CustomExceptionStatus.ALREADY_TRADE_AUCTION);
        }

        return response;
    }

    // 경매 거래 취소
    @Override
    public void cancelAuction(Long auctionIdx) {
        AuctionEntity auctionEntity = auctionRepository.findById(auctionIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_INVALID));

        // 경매 상태 진행 중으로 변경
        auctionEntity.setAuctionStatus(AuctionStatus.IN_PROGRESS.getStatus());

        //가장 높은 입찰 삭제
        // 입찰 목록을 입찰 금액에 내림차순으로 정렬해서 가져옴-
        List<AuctionBid> auctionBidList = auctionBidRepository
                .findByAuctionIdxOrderByAuctionBidPriceDesc(auctionEntity.getAuctionIdx());

        //입잘자가 혼자라면 초기화
//        if(auctionBidList.size() == 1){
//            auction.setAuctionHighestBid(0);
//            auction.setHighestBidUser(null);
//        }else { //입찰자보다 바로 아래 입찰금액으로 갱신
//            auction.setAuctionHighestBid(auctionBidList.get(1).getAuctionBidPrice());
//            auction.setHighestBidUser(auctionBidList.get(1).getUser());
//        }

        auctionBidRepository.deleteByUser_UserIdxAndAuction_AuctionIdx(auctionBidList.get(0).getUserEntity().getUserIdx(), auctionIdx);
        log.info("거래 취소 완료");
    }

    // 경매 거래 확정
    @Override
    public void confirmAuction(Long auctionIdx) {
        AuctionEntity auctionEntity = auctionRepository.findById(auctionIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_INVALID));
        GifticonEntity gifticonEntity = gifticonRepository.findById(auctionEntity.getGifticonEntity().getGifticonIdx())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.GIFTICON_INVALID));
        UserEntity userEntity = userRepository.findById(0L)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        auctionEntity.setAuctionStatus(AuctionStatus.AWARDED.getStatus());

        for(AuctionBid bid : auctionEntity.getAuctionBidList()) {
            if(bid.getUserEntity().getUserIdx().equals(userEntity.getUserIdx())){
                bid.setAuctionBidStatus(BidStatus.AWARDED.getStatus());
            }
            else {
                bid.setAuctionBidStatus(BidStatus.FAILURE.getStatus());
            }
        }

        gifticonEntity.setUserEntity(userEntity);
        gifticonEntity.setGifticonStatus(GifticonStatus.KEEP.getStatus());
        auctionEntity.setAuctionCompletedDate(LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public AuctionConfirmResponse getConfirmAuction(Long auctionIdx) {
        AuctionEntity auctionEntity = auctionRepository.findById(auctionIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_INVALID));

        AuctionConfirmResponse response = AuctionMapper.INSTANCE.auctionToConfirm(auctionEntity);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public AuctionConfirmBuyResponse getConfirmBuyAuction(Long auctionIdx) {
        AuctionEntity auctionEntity = auctionRepository.findById(auctionIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_INVALID));

        auctionEntity.setNotificationCreatedDate(LocalDateTime.now());

        AuctionConfirmBuyResponse response = AuctionMapper.INSTANCE.auctionToConfirmBuy(auctionEntity);

        return response;
    }


    public List<AuctionEntity> getAuctionConfirmList() {
        List<AuctionEntity> auctionEntities = auctionRepository.findByAuctionListConfirm();

        return auctionEntities;
    }

    @Override
    public List<AuctionEntity> getAuctionExpiredList() {
        return auctionRepository.findAuctionAllExpired();
    }

    @Override
    public void rejectAuction(Long auctionIdx) {
        AuctionEntity auctionEntity = auctionRepository.findById(auctionIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_INVALID));
        GifticonEntity gifticonEntity = gifticonRepository.findById(auctionEntity.getGifticonEntity().getGifticonIdx())
                        .orElseThrow(() -> new CustomException(CustomExceptionStatus.GIFTICON_INVALID));

        auctionEntity.setAuctionCompletedDate(auctionEntity.getAuctionEndDate());
        auctionEntity.setAuctionStatus(AuctionStatus.EXPIRED.getStatus());
        gifticonEntity.setGifticonStatus(GifticonStatus.KEEP.getStatus());
    }

    @Override
    public List<AuctionEntity> getPopularAuction() {
        List<AuctionEntity> auctionEntities = auctionRepository.findAuctionList();

        List<AuctionEntity> auctionEntityList = new ArrayList<>();
        if(auctionEntities.size() >= 1) {
            auctionEntityList.add(auctionEntities.get(0));
        }

        if(auctionEntities.size() >= 2) {
            auctionEntityList.add(auctionEntities.get(1));
        }

        return auctionEntityList;
    }

    @Override
    public List<Integer> getMainCategory() {
        List<AuctionEntity> auctionEntities = auctionRepository.findAwardedAuctionList();

        int[] mainCategoryCount = new int[6];

        for(AuctionEntity auctionEntity : auctionEntities) {
            int idx = auctionEntity.getGifticonEntity().getMainCategoryEntity().getMainCategoryIdx();
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
        List<AuctionEntity> auctionEntities = auctionRepository.findAwardedAuctionList();

        int[] subCategoryCount = new int[14];

        for(AuctionEntity auctionEntity : auctionEntities) {
            long idx = auctionEntity.getGifticonEntity().getSubCategoryEntity().getSubCategoryIdx();
            subCategoryCount[(int)idx]++;
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
