package com.conseller.conseller.core.user.implement;

import com.conseller.conseller.core.user.domain.User;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import com.conseller.conseller.core.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Component
@RequiredArgsConstructor
public class UserAppender {
    private final UserRepository userRepository;
    private final UserFinder userFinder;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long append(User user) {
        userValidator.validateSignUp(user);

        UserEntity userEntity = UserEntity.of(user);
        userEntity.addUserRole();
        userEntity.encryptPassword(passwordEncoder);

        return userRepository.save(userEntity).getUserIdx();
    }

    @Transactional
    public void appendPattern(long userIdx,  String pattern) {
        User user = userFinder.findValidUser(userIdx);
        user.updatePattern(pattern);
        userRepository.save(UserEntity.of(user));
    }
}
