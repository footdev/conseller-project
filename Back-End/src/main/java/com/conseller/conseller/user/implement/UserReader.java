package com.conseller.conseller.user.implement;

import com.conseller.conseller.exception.CustomException;
import com.conseller.conseller.exception.CustomExceptionStatus;
import com.conseller.conseller.user.domain.User;
import com.conseller.conseller.user.infrastructure.UserEntity;
import com.conseller.conseller.user.infrastructure.UserRepository;
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
