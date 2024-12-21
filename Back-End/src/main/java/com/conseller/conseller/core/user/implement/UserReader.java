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

    public User read(long userIdx) {
        return userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.NOT_EXIST_USER))
                .toDomain();
    }

    public User read(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.NOT_EXIST_USER))
                .toDomain();
    }

    public User readByIdAndEmail(String id, String email) {
        return userRepository.findByUserEmailAndUserId(email, id)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.NOT_EXIST_USER))
                .toDomain();
    }

    public User readByNameAndEmail(String userName, String userEmail) {
        return userRepository.findByUserEmailAndUserName(userEmail, userName)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.NOT_EXIST_USER))
                .toDomain();
    }

    public boolean existsByUserNickname(String nickname) {
        return userRepository.existsByUserNickname(nickname);
    }

    public boolean existsByUserEmail(String email) {
        return userRepository.existsByUserNickname(email);
    }

    public boolean existsById(String id) {
        return userRepository.existsByUserNickname(id);
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByUserNickname(phoneNumber);
    }
}
