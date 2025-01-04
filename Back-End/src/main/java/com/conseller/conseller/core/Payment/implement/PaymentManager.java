package com.conseller.conseller.core.Payment.implement;

import com.conseller.conseller.core.auction.domain.Auction;
import com.conseller.conseller.core.auction.domain.AuctionOrder;
import com.conseller.conseller.core.user.domain.User;
import com.conseller.conseller.core.user.implement.UserAppender;
import com.conseller.conseller.core.user.implement.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentManager {
    private final PaymentValidator paymentValidator;
    private final UserAppender userAppender;

    public void pay(User user, int price) {
        paymentValidator.isEnoughCash(user, price);
        user.decreaseCash(price);
        userAppender.append(user);
    }

    public void depositFondsToSeller(User seller, AuctionOrder auctionOrder) {
        seller.increaseCash(auctionOrder.getPrice());
        userAppender.append(seller);
    }
}
