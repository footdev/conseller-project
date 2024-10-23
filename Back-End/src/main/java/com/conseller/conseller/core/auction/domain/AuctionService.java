package com.conseller.conseller.core.auction.domain;

import com.conseller.conseller.core.auction.api.dto.request.AuctionListRequest;
import com.conseller.conseller.core.auction.api.dto.request.ModifyAuctionRequest;
import com.conseller.conseller.core.auction.api.dto.request.RegistAuctionRequest;
import com.conseller.conseller.core.auction.api.dto.response.*;
import com.conseller.conseller.core.auction.implement.*;
import com.conseller.conseller.core.gifticon.domain.Gifticon;
import com.conseller.conseller.core.gifticon.implement.GifticonReader;
import com.conseller.conseller.core.gifticon.implement.GifticonUpdater;
import com.conseller.conseller.core.gifticon.implement.GifticonValidator;
import com.conseller.conseller.core.user.domain.User;
import com.conseller.conseller.core.user.implement.UserReader;
import com.conseller.conseller.core.gifticon.infrastructure.enums.GifticonStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuctionService {
    private final AuctionReader auctionReader;
    private final AuctionAppender auctionAppender;
    private final AuctionUpdater auctionUpdater;
    private final AcutionRemover acutionRemover;
    private final AuctionProcessor auctionProcessor;

    private final UserReader userReader;
    private final GifticonReader gifticonReader;
    private final GifticonValidator gifticonValidator;
    private final GifticonUpdater gifticonUpdater;

    public AuctionListResponse getAuctions(long id, AuctionListRequest request) {
        return new AuctionListResponse(auctionReader.readAuctions(id, request));
    }

    @Transactional
    public long registAuction(RegistAuctionRequest request) {
        User user = userReader.read(request.getUserIdx());
        Gifticon gifticon = gifticonReader.read(request.getGifticonIdx());
        gifticonValidator.isKeep(gifticon);
        gifticonUpdater.updateStatus(gifticon, GifticonStatus.AUCTION);
        return auctionAppender.append(Auction.of(request, user, gifticon));
    }

    public DetailAuctionResponse detailAuction(long auctionIdx) {
        return DetailAuctionResponse.of(auctionReader.read(auctionIdx));
    }

    public void modifyAuction(long id, ModifyAuctionRequest auctionRequest) {
        auctionUpdater.updateAuction(id, auctionRequest.getAuctionText());
    }

    public void deleteAuction(long id) {
        acutionRemover.remove(id);
    }
//
//    // 경매 거래 진행
//    public AuctionTradeResponse tradeAuction(long auctionId, long userId) {
//        Auction auction = auctionReader.read(auctionId);
//        User host = userReader.read(userId);
//        AuctionTradeResponse response = null;
//
//        if(auctionEntity.getAuctionStatus().equals(AuctionStatus.IN_PROGRESS.getStatus())) {
//            if (!auctionEntity.getUserEntity().getUserIdx().equals(userIdx)) { //즉시 구매자 라면
//
//                boolean isExist = false;
//                Long bidIdx = 0L;

                // 이미 입찰이 있고 그 입찰이 지금 경매라면
//                for (AuctionBid bid : auction.getAuctionBidList()) {
//                    if (bid.getUser().getUserIdx().equals(userIdx)) {
//                        isExist = true;
//                        bidIdx = bid.getAuctionBidIdx();
//                    }
//                }
//
//                if (isExist) {
//                    AuctionBidEntity auctionBidEntity = auctionBidRepository.findById(bidIdx)
//                            .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_BID_INVALID));
//
//                    // 입찰 정보 수정
//                    auctionBidEntity.setAuctionBidPrice(auctionEntity.getUpperPrice());
//                    auctionBidEntity.setAuctionEntity(auctionEntity);
//                } else { // 없으면
//                    AuctionBidEntity auctionBidEntity = AuctionBidMapper.INSTANCE.registImToAuctionBid(userEntity, auctionEntity, auctionEntity.getUpperPrice());
//
//                    // 새로 등록
//                    auctionBidRepository.save(auctionBidEntity);
//                }
//            }
//
//            // 경매 상태 거래중으로 변경
//            auctionEntity.setAuctionStatus(AuctionStatus.IN_TRADE.getStatus());

            //입찰 상태를 낙찰 예정으로 변경
//            AuctionBid bid = auctionBidRepository.findByUser_UserIdxAndAuction_AuctionIdx(auction.getHighestBidUser().getUserIdx(), auctionIdx)
//                    .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_BID_INVALID));

//            bid.setAuctionBidStatus(BidStatus.EXPECTED.getStatus());

            // 판매자의 계좌번호와 은행 전달
//            response = new AuctionTradeResponse(auctionEntity.getUserEntity().getUsername() , auctionEntity.getUserEntity().getUserAccount(),
//                    auctionEntity.getUserEntity().getUserAccountBank());
//        }
//        else {
//            throw new CustomException(CustomExceptionStatus.ALREADY_TRADE_AUCTION);
//        }
//
//        return response;
//    }

    // 경매 거래 취소
//    public void cancelAuction(Long auctionIdx) {
//        AuctionEntity auctionEntity = auctionRepository.findById(auctionIdx)
//                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_INVALID));
//
//        // 경매 상태 진행 중으로 변경
//        auctionEntity.setAuctionStatus(AuctionStatus.IN_PROGRESS.getStatus());
//
//        //가장 높은 입찰 삭제
//        // 입찰 목록을 입찰 금액에 내림차순으로 정렬해서 가져옴-
//        List<AuctionBidEntity> auctionBidEntityList = auctionBidRepository
//                .findByAuctionIdxOrderByAuctionBidPriceDesc(auctionEntity.getAuctionIdx());

        //입잘자가 혼자라면 초기화
//        if(auctionBidList.size() == 1){
//            auction.setAuctionHighestBid(0);
//            auction.setHighestBidUser(null);
//        }else { //입찰자보다 바로 아래 입찰금액으로 갱신
//            auction.setAuctionHighestBid(auctionBidList.get(1).getAuctionBidPrice());
//            auction.setHighestBidUser(auctionBidList.get(1).getUser());
//        }
//
//        auctionBidRepository.deleteByUser_UserIdxAndAuction_AuctionIdx(auctionBidEntityList.get(0).getUserEntity().getUserIdx(), auctionIdx);
//    }

    // 경매 거래 확정
//    public void confirmAuction(Long auctionIdx) {
//        AuctionEntity auctionEntity = auctionRepository.findById(auctionIdx)
//                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_INVALID));
//        GifticonEntity gifticonEntity = gifticonRepository.findById(auctionEntity.getGifticonEntity().getGifticonIdx())
//                .orElseThrow(() -> new CustomException(CustomExceptionStatus.GIFTICON_INVALID));
//        UserEntity userEntity = userRepository.findById(0L)
//                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));
//
//        auctionEntity.setAuctionStatus(AuctionStatus.AWARDED.getStatus());
//
//        for(AuctionBidEntity bid : auctionEntity.getAuctionBidEntityList()) {
//            if(bid.getUserEntity().getUserIdx().equals(userEntity.getUserIdx())){
//                bid.setAuctionBidStatus(BidStatus.AWARDED.getStatus());
//            }
//            else {
//                bid.setAuctionBidStatus(BidStatus.FAILURE.getStatus());
//            }
//        }
//
//        gifticonEntity.setUserEntity(userEntity);
//        gifticonEntity.setGifticonStatus(GifticonStatus.KEEP.getStatus());
//        auctionEntity.setAuctionCompletedDate(LocalDateTime.now());
//    }
//
//    @Transactional(readOnly = true)
//    public AuctionConfirmResponse getConfirmAuction(Long auctionIdx) {
//        AuctionEntity auctionEntity = auctionRepository.findById(auctionIdx)
//                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_INVALID));
//
//        AuctionConfirmResponse response = AuctionMapper.INSTANCE.auctionToConfirm(auctionEntity);
//
//        return response;
//    }
//
//    @Transactional(readOnly = true)
//    public AuctionConfirmBuyResponse getConfirmBuyAuction(Long auctionIdx) {
//        AuctionEntity auctionEntity = auctionRepository.findById(auctionIdx)
//                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_INVALID));
//
//        auctionEntity.setNotificationCreatedDate(LocalDateTime.now());
//
//        AuctionConfirmBuyResponse response = AuctionMapper.INSTANCE.auctionToConfirmBuy(auctionEntity);
//
//        return response;
//    }
//
//
//    public List<AuctionEntity> getAuctionConfirmList() {
//        List<AuctionEntity> auctionEntities = auctionRepository.findByAuctionListConfirm();
//
//        return auctionEntities;
//    }
//
//    public List<AuctionEntity> getAuctionExpiredList() {
//        return auctionRepository.findAuctionAllExpired();
//    }
//
//    public void rejectAuction(Long auctionIdx) {
//        AuctionEntity auctionEntity = auctionRepository.findById(auctionIdx)
//                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_INVALID));
//        GifticonEntity gifticonEntity = gifticonRepository.findById(auctionEntity.getGifticonEntity().getGifticonIdx())
//                        .orElseThrow(() -> new CustomException(CustomExceptionStatus.GIFTICON_INVALID));
//
//        auctionEntity.setAuctionCompletedDate(auctionEntity.getAuctionEndDate());
//        auctionEntity.setAuctionStatus(AuctionStatus.EXPIRED.getStatus());
//        gifticonEntity.setGifticonStatus(GifticonStatus.KEEP.getStatus());
//    }
//
//    public List<AuctionEntity> getPopularAuction() {
//        List<AuctionEntity> auctionEntities = auctionRepository.findAuctionList();
//
//        List<AuctionEntity> auctionEntityList = new ArrayList<>();
//        if(auctionEntities.size() >= 1) {
//            auctionEntityList.add(auctionEntities.get(0));
//        }
//
//        if(auctionEntities.size() >= 2) {
//            auctionEntityList.add(auctionEntities.get(1));
//        }
//
//        return auctionEntityList;
//    }
//
//    public List<Integer> getMainCategory() {
//        List<AuctionEntity> auctionEntities = auctionRepository.findAwardedAuctionList();
//
//        int[] mainCategoryCount = new int[6];
//
//        for(AuctionEntity auctionEntity : auctionEntities) {
//            long idx = auctionEntity.getGifticonEntity().getMainCategoryEntity().getMainCategoryIdx();
//            mainCategoryCount[(int)idx]++;
//        }
//
//        int maxIdx = 1;
//        for(int i = 1; i < 6; i++) {
//            if(mainCategoryCount[i] > mainCategoryCount[maxIdx]){
//                maxIdx = i;
//            }
//        }
//
//        int secondIdx = 2;
//        for(int i = 1; i < 6; i++) {
//            if(maxIdx != i && mainCategoryCount[i] > mainCategoryCount[maxIdx]){
//                secondIdx = i;
//            }
//        }
//
//        List<Integer> list = new ArrayList<>();
//        list.add(maxIdx);
//        list.add(secondIdx);
//
//        return list;
//    }
//
//    public List<Integer> getSubCategory() {
//        List<AuctionEntity> auctionEntities = auctionRepository.findAwardedAuctionList();
//
//        int[] subCategoryCount = new int[14];
//
//        for(AuctionEntity auctionEntity : auctionEntities) {
//            long idx = auctionEntity.getGifticonEntity().getSubCategoryEntity().getSubCategoryIdx();
//            subCategoryCount[(int)idx]++;
//        }
//
//        int maxIdx = 1;
//        for(int i = 1; i < 14; i++) {
//            if(subCategoryCount[i] > subCategoryCount[maxIdx]){
//                maxIdx = i;
//            }
//        }
//
//        int secondIdx = 2;
//        for(int i = 1; i < 14; i++) {
//            if(maxIdx != i && subCategoryCount[i] > subCategoryCount[maxIdx]){
//                secondIdx = i;
//            }
//        }
//
//        List<Integer> list = new ArrayList<>();
//        list.add(maxIdx);
//        list.add(secondIdx);
//
//        return list;
//    }

}
