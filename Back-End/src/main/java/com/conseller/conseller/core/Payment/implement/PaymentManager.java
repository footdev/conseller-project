package com.conseller.conseller.core.Payment.implement;

import com.conseller.conseller.core.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentManager {
    private final PaymentValidator paymentValidator;

    public void pay(User user, int price) {
        paymentValidator.isEnoughCash(user, price);
        user.decreaseCash(price);
    }
}
