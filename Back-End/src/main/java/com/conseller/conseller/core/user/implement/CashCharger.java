package com.conseller.conseller.core.user.implement;

import com.conseller.conseller.core.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CashCharger {
    private final UserReader userReader;
    private final UserAppender userAppender;

    public void chargeCash(long userId, long cash) {
        User user = userReader.read(userId);
        user.chargeCash(cash);
        userAppender.append(user);
    }
}
