package com.conseller.conseller.core.barter.implement;

import com.conseller.conseller.core.barter.domain.Barter;
import com.conseller.conseller.core.barter.domain.enums.BarterStatus;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@Component
public class BarterValidator {
    public void isExchangeable(Barter barter) {
        if (!BarterStatus.EXCHANGEABLE.equals(barter.getBarterStatus())) {
            throw new CustomException(CustomExceptionStatus.BARTER_INVALID_STATUS);
        }
    }
}
