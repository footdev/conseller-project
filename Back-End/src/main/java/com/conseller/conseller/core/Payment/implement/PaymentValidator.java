package com.conseller.conseller.core.Payment.implement;

import com.conseller.conseller.core.user.domain.User;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import org.springframework.stereotype.Component;

@Component
public class PaymentValidator {

    public void isEnoughCash(User buyer, long price) {
        if (buyer.getUserCash() < price) {
            throw new CustomException(CustomExceptionStatus.NOT_ENOUGH_CASH);
        }
    }
}
