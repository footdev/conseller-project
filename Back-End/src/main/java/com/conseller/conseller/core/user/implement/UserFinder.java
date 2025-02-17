package com.conseller.conseller.core.user.implement;

import com.conseller.conseller.core.user.domain.TargetUser;
import com.conseller.conseller.core.user.domain.User;
import com.conseller.conseller.core.user.infrastructure.UserRepository;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFinder {
    private final UserRepository userRepository;
    private UserReader userReader;
    private UserValidator userValidator;

    public User findValidUser(long id) {
        User user = userRepository.findByUserIdx(id)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.NOT_EXIST_USER))
                .toDomain();

        userValidator.validateUser(user);

        return user;
    }

    public void equals(User seller, User buyer) {
        if (seller.getUserIdx() != buyer.getUserIdx()) {
            throw new CustomException(CustomExceptionStatus.INVALID_SUCCESS_BID_USER);
        }
    }

    public User findLoginUser(TargetUser targetUser) {
        userValidator.checkIdAndPassword(targetUser);
        User user = userReader.read(targetUser.getUserId());
        userValidator.validateUser(user);
        return user;
    }
}
