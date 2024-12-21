package com.conseller.conseller.core.user.implement;

import com.conseller.conseller.core.user.domain.TargetUser;
import com.conseller.conseller.core.user.domain.User;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import com.conseller.conseller.core.user.infrastructure.UserRepository;
import com.conseller.conseller.core.user.domain.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void validateSignUp(User user) {
        boolean idExists = userRepository.existsByUserId(user.getUserId());
        boolean nicknameExists = userRepository.existsByUserNickname(user.getUserNickname());
        boolean emailExists = userRepository.existsByUserEmail(user.getUserEmail());
        boolean phoneNumber = userRepository.existsByUserPhoneNumber(user.getUserPhoneNumber());

        if (idExists) {
            throw new CustomException(CustomExceptionStatus.WRONG_ID);
        }

        if (nicknameExists) {
            throw new CustomException(CustomExceptionStatus.ALREADY_EXIST_NICKNAME);
        }

        if (emailExists) {
            throw new CustomException(CustomExceptionStatus.ALREADY_EXIST_EMAIL);
        }

        if (phoneNumber) {
            throw new CustomException(CustomExceptionStatus.ALREADY_EXIST_PHONE_NUMBER);
        }
    }

    public void checkIdAndPassword(TargetUser targetUser) {
        UserEntity userEntity = userRepository.findByUserId(targetUser.getUserId())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.WRONG_ID));

        if (!userEntity.checkPassword(passwordEncoder, targetUser.getUserPassword())) {
            throw new CustomException(CustomExceptionStatus.WRONG_PW);
        }
    }

    public void validateUser(User user) {
        if (UserStatus.RESTRICTED.getStatus().equals(user.getUserStatus())) {
            throw new CustomException(CustomExceptionStatus.RESTRICT_USER);
        }

        if (user.getUserDeletedDate() != null) {
            throw new CustomException(CustomExceptionStatus.RESTRICT_USER);
        }
    }

    public void existsUserByPassword(long userIdx, String password) {
        if(userRepository.existsByUserIdxAndUserPassword(userIdx, passwordEncoder.encode(password))) {
            throw new CustomException(CustomExceptionStatus.WRONG_PW);
        }
    }

    public void checkPattern(User user, String pattern) {
        if (!user.getUserPattern().equals(pattern)) {
            throw new CustomException(CustomExceptionStatus.NOT_EXIST_USER);
        }
    }
}
