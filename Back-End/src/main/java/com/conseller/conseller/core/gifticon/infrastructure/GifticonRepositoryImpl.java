package com.conseller.conseller.core.gifticon.infrastructure;

import com.conseller.conseller.core.gifticon.api.dto.response.ExpiringGifticonResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;

import static com.conseller.conseller.entity.QGifticon.gifticon;

@Repository
@RequiredArgsConstructor
public class GifticonRepositoryImpl {

    private final JPAQueryFactory jpaQueryFactory;

    //구현
    public List<ExpiringGifticonResponse> getUserIdxAndExpiringGifticonCount() {
        NumberExpression<Integer> daysLeft = gifticon.gifticonEndDate.dayOfYear().subtract(LocalDate.now().getDayOfYear());

        return jpaQueryFactory
                .select(Projections.constructor(ExpiringGifticonResponse.class,
                        gifticon.user.userIdx,
                        daysLeft,
                        gifticon.gifticonName.max(),
                        gifticon.gifticonName.count()))
                .from(gifticon)
                .where(daysLeft.in(1, 7))
                .groupBy(gifticon.user.userIdx, daysLeft)
                .orderBy(gifticon.user.userIdx.asc(), daysLeft.asc())
                .fetch();
    }
}
