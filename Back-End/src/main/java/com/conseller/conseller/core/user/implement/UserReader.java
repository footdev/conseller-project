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

    public User read(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID))
                .toDomain();
    }
}
