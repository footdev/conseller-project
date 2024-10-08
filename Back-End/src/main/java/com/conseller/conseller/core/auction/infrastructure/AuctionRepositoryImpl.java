package com.conseller.conseller.core.auction.infrastructure;

import com.conseller.conseller.core.auction.api.dto.request.AuctionListRequest;
import com.conseller.conseller.core.auction.auction.AuctionOrderStatus;
import com.conseller.conseller.core.auction.domain.enums.AuctionStatus;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.conseller.conseller.entity.QAuction.auction;

@Repository
@RequiredArgsConstructor
public class AuctionRepositoryImpl {
    private final JPAQueryFactory factory;

    public List<AuctionEntity> findAuctionListByCursor(AuctionEntity cursor, AuctionListRequest req) {
        return factory
                .selectFrom(auction)
                .innerJoin(auction.gifticon)
                .fetchJoin()
                .where(
                        eqCategory(req.getMainCategory(), req.getSubCategory()),
                        auction.auctionStatus.eq(AuctionStatus.IN_PROGRESS.getStatus()),
                        cursorFieldAndIdx(cursor, req)
                )
                .orderBy(orderSpecifier(req.getStatus()), auction.auctionIdx.asc())
                .limit(10)
                .fetch();
    }

    public Page<AuctionEntity> findAuctionList(AuctionListRequest request, Pageable pageable) {
        List<AuctionEntity> content = factory
                .selectFrom(auction)
                .innerJoin(auction.gifticon)
                .where(
                        eqCategory(request.getMainCategory(), request.getSubCategory()),
                        eqSearch(request.getSearchQuery()),
                        auction.auctionStatus.eq(AuctionStatus.IN_PROGRESS.getStatus())
                )
                .orderBy(orderSpecifier(request.getStatus()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = factory
                .select(auction.count())
                .from(auction)
                .where(
                        eqCategory(request.getMainCategory(), request.getSubCategory()),
                        eqSearch(request.getSearchQuery()),
                        auction.auctionStatus.eq(AuctionStatus.IN_PROGRESS.getStatus())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }

    private BooleanExpression eqCategory(Integer mainCategory, Integer subCategory) {
        if(subCategory != 0) {
            return auction.gifticon.subCategory.subCategoryIdx.eq(subCategory);
        }else if(mainCategory != 0 && subCategory == 0) {
            return auction.gifticon.mainCategory.mainCategoryIdx.eq(mainCategory);
        }else {
            return null;
        }
    }

    private BooleanExpression eqSearch(String searchQuery) {
        if(!StringUtils.hasText(searchQuery)){
            return null;
        }

        return auction.gifticon.gifticonName.containsIgnoreCase(searchQuery);
    }



    private OrderSpecifier orderSpecifier(Integer status) {
        if(status == 0) {
            return new OrderSpecifier(Order.ASC, auction.auctionStartDate);
        } else if (status == 1) {
            return new OrderSpecifier(Order.ASC, auction.gifticon.gifticonEndDate);
        } else if(status == 2) {
            return new OrderSpecifier(Order.ASC, auction.highestBid);
        } else  {
            return new OrderSpecifier(Order.ASC, auction.upperPrice);
        }
    }

    private BooleanExpression cursorFieldAndIdx(AuctionEntity cursor, AuctionListRequest req) {
        if (cursor == null || req.getStatus() == null) {
            return null;
        }

        switch (AuctionOrderStatus.findByStatus(req.getStatus())) {
            case START_DATE:
                return auction.auctionStartDate.eq(cursor.getAuctionStartDate())
                        .and(auction.auctionIdx.gt(cursor.getAuctionIdx()))
                        .or(auction.auctionStartDate.gt(cursor.getAuctionStartDate()));

            case END_DATE:
                return auction.gifticon.gifticonEndDate.eq(cursor.getGifticonEntity().getGifticonEndDate())
                        .and(auction.auctionIdx.gt(cursor.getAuctionIdx()))
                        .or(auction.gifticon.gifticonEndDate.gt(cursor.getGifticonEntity().getGifticonEndDate()));

            case HIGHEST_BID:
                return auction.highestBid.eq(cursor.getHighestBid())
                        .and(auction.auctionIdx.gt(cursor.getAuctionIdx()))
                        .or(auction.highestBid.auctionBidPrice.gt(cursor.getHighestBid().getAuctionBidPrice()));

            case UPPER_PRICE:
                return auction.upperPrice.eq(cursor.getUpperPrice())
                        .and(auction.auctionIdx.gt(cursor.getAuctionIdx()))
                        .or(auction.upperPrice.gt(cursor.getUpperPrice()));
        }

        return null;
    }
}
