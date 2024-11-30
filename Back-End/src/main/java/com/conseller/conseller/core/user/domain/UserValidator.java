package com.conseller.conseller.core.user.domain;

import com.conseller.conseller.core.user.infrastructure.UserEntity;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import com.conseller.conseller.core.user.infrastructure.UserRepository;
import com.conseller.conseller.core.user.api.dto.request.LoginRequest;
import com.conseller.conseller.core.user.api.dto.request.SignUpRequest;
import com.conseller.conseller.core.user.domain.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void signUpDtoValidate(final SignUpRequest signUpRequest) {
        boolean idExists = userRepository.existsByUserId(signUpRequest.getUserId());
        boolean nicknameExists = userRepository.existsByUserNickname(signUpRequest.getUserNickname());
        boolean emailExists = userRepository.existsByUserEmail(signUpRequest.getUserEmail());
        boolean phoneNumber = userRepository.existsByUserPhoneNumber(signUpRequest.getUserPhoneNumber());

        if (idExists) {
            throw new CustomException(CustomExceptionStatus.WRONG_ID);
        }

        if (nicknameExists) {
            throw new CustomException(CustomExceptionStatus.EXIST_NICKNAME);
        }

        if (emailExists) {
            throw new CustomException(CustomExceptionStatus.EXIST_EMAIL);
        }

        if (phoneNumber) {
            throw new CustomException(CustomExceptionStatus.EXIST_PHONE_NUMBER);
        }
    }

    public boolean isVaildByUserIdx(long userIdx) {
        return userRepository.existsByUserIdx(userIdx);
    }

    public UserEntity validateLogin(LoginRequest request) {
        // 입력 id 정보가 유효한지 확인
        UserEntity userEntity = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.WRONG_ID));

        // 입력한 password 정보가 유효한지 확인
        if (!userEntity.checkPassword(new BCryptPasswordEncoder(), request.getUserPassword())) {
            throw new CustomException(CustomExceptionStatus.WRONG_PW);
        }

        return userEntity;
    }

    //해당 유저가 서비스 이용이 가능한 유저인지 확인
    public void validateUser(UserEntity userEntity) {
        // 입력한 유저가 사용 제한된 유저인지 확인
        if (UserStatus.RESTRICTED.getStatus().equals(userEntity.getUserStatus())) {
            throw new CustomException(CustomExceptionStatus.RESTRICT);
        }

        // 입력한 유저가 탈퇴한 유저인지 확인
        if (userEntity.getUserDeletedDate() != null) {
            throw new CustomException(CustomExceptionStatus.RESTRICT);
        }
    }
}
