package com.conseller.conseller.core.auction.domain;

import com.conseller.conseller.core.auction.api.dto.request.*;
import com.conseller.conseller.core.auction.api.dto.response.*;
import com.conseller.conseller.core.auction.domain.enums.AuctionStatus;
import com.conseller.conseller.core.auction.implement.*;
import com.conseller.conseller.core.bid.infrastructure.AuctionBidEntity;
import com.conseller.conseller.core.gifticon.domain.Gifticon;
import com.conseller.conseller.core.gifticon.implement.GifticonReader;
import com.conseller.conseller.core.gifticon.implement.GifticonUpdater;
import com.conseller.conseller.core.gifticon.implement.GifticonValidator;
import com.conseller.conseller.core.user.domain.User;
import com.conseller.conseller.core.user.implement.UserReader;
import com.conseller.conseller.core.gifticon.domain.enums.GifticonStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuctionService {
    private final AuctionReader auctionReader;
    private final AuctionAppender auctionAppender;
    private final AuctionUpdater auctionUpdater;
    private final AuctionRemover auctionRemover;
    private final AuctionProcessor auctionProcessor;

    private final UserReader userReader;
    private final GifticonReader gifticonReader;
    private final GifticonValidator gifticonValidator;
    private final GifticonUpdater gifticonUpdater;
    private AuctionValidator auctionValidator;

    public List<AuctionItemData> getAuctions(long id, AuctionListRequest request) {
        return auctionReader.read(id, request);
    }

    @Transactional
    public long registAuction(RegistAuctionRequest request) {
        User user = userReader.read(request.getUserIdx());
        Gifticon gifticon = gifticonReader.read(request.getGifticonIdx());
        gifticonValidator.isKeep(gifticon);
        gifticonUpdater.updateStatus(gifticon, GifticonStatus.AUCTION);
        return auctionAppender.append(Auction.of(request, user, gifticon));
    }

    public Auction detailAuction(long auctionIdx) {
        return auctionReader.read(auctionIdx);
    }

    public void modifyAuction(long id, ModifyAuctionRequest auctionRequest) {
        auctionUpdater.updateAuction(id, auctionRequest.getAuctionText());
    }

    public void deleteAuction(long id) {
        auctionRemover.remove(id);
    }

     //경매 거래 확정
    public void confirmAuction(Long auctionIdx) {
        AuctionEntity auctionEntity = auctionRepository.findById(auctionIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_INVALID));
        GifticonEntity gifticonEntity = gifticonRepository.findById(auctionEntity.getGifticonEntity().getGifticonIdx())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.GIFTICON_INVALID));
        UserEntity userEntity = userRepository.findById(0L)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        auctionEntity.setAuctionStatus(AuctionStatus.AWARDED.getStatus());

        for(AuctionBidEntity bid : auctionEntity.getAuctionBidEntityList()) {
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

    @Transactional(readOnly = true)
    public AuctionConfirmResponse getConfirmAuction(Long auctionIdx) {
        AuctionEntity auctionEntity = auctionRepository.findById(auctionIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_INVALID));

        AuctionConfirmResponse response = AuctionMapper.INSTANCE.auctionToConfirm(auctionEntity);

        return response;
    }

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

    public List<AuctionEntity> getAuctionExpiredList() {
        return auctionRepository.findAuctionAllExpired();
    }

    public void rejectAuction(Long auctionIdx) {
        AuctionEntity auctionEntity = auctionRepository.findById(auctionIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.AUCTION_INVALID));
        GifticonEntity gifticonEntity = gifticonRepository.findById(auctionEntity.getGifticonEntity().getGifticonIdx())
                        .orElseThrow(() -> new CustomException(CustomExceptionStatus.GIFTICON_INVALID));

        auctionEntity.setAuctionCompletedDate(auctionEntity.getAuctionEndDate());
        auctionEntity.setAuctionStatus(AuctionStatus.EXPIRED.getStatus());
        gifticonEntity.setGifticonStatus(GifticonStatus.KEEP.getStatus());
    }

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

    public List<Integer> getMainCategory() {
        List<AuctionEntity> auctionEntities = auctionRepository.findAwardedAuctionList();

        int[] mainCategoryCount = new int[6];

        for(AuctionEntity auctionEntity : auctionEntities) {
            long idx = auctionEntity.getGifticonEntity().getMainCategoryEntity().getMainCategoryIdx();
            mainCategoryCount[(int)idx]++;
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

    // 즉시 구매 진행
    public void buyNowPrice(TargetAuction targetAuction, TargetBuyer targetBuyer) {
        auctionProcessor.buyNow(targetAuction, targetBuyer);
    }

    // 낙찰자가 낙찰을 확정했으므로 기프티콘이 지급되고 낙찰 확정이 된다.
    public void confirmWinningBid(TargetAuction targetAuction, TargetBuyer targetBuyer) {
        auctionProcessor.buy(targetAuction, targetBuyer);
    }
}
