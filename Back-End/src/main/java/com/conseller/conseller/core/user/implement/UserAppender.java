package com.conseller.conseller.core.user.implement;

import com.conseller.conseller.core.user.domain.User;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import com.conseller.conseller.core.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAppender {
    private final UserRepository userRepository;

    public void append(User user) {
        userRepository.save(UserEntity.of(user));
    }
}
