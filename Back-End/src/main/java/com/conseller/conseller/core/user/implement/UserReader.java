package com.conseller.conseller.core.user.implement;

import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import com.conseller.conseller.core.user.domain.User;
import com.conseller.conseller.core.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReader {

    private final UserRepository userRepository;

    public User readUser(long userId) {
        return User.of(
                userRepository.findById(userId)
                        .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID))
        );
    }
}
