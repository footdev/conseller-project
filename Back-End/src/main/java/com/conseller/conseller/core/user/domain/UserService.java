package com.conseller.conseller.core.user.domain;

import com.conseller.conseller.core.user.api.dto.request.*;
import com.conseller.conseller.core.user.api.dto.response.*;
import com.conseller.conseller.core.user.auth.implement.AuthProcessor;
import com.conseller.conseller.core.user.implement.*;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import com.conseller.conseller.core.user.domain.enums.LoginStatus;
import com.conseller.conseller.global.security.implement.BlackListManager;
import com.conseller.conseller.global.security.implement.TokenValidator;
import com.conseller.conseller.global.security.infrastructure.JwtTokenProvider;
import com.conseller.conseller.global.utils.TemporaryValueGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserReader userReader;
    private final UserFinder userFinder;
    private final UserValidator userValidator;
    private final UserAppender userAppender;
    private final UserUpdater userUpdater;
    private final UserProcessor userProcessor;
    private final CashCharger cashCharger;
    private final AuthProcessor authProcessor;
    private final BlackListManager blackListManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenValidator tokenValidator;

    public Long signUp(User user) {
        return userAppender.append(user);
    }

    public LoginResponse login(TargetUser targetUser) {
        User loginUser = userFinder.findLoginUser(targetUser);
       return authProcessor.authenticateAndGetToken(loginUser, LoginStatus.GENERAL);
    }

    public void updateUserInfo(long userIdx, UserInfoRequest userInfoRequest) {
        userUpdater.update(userIdx, userInfoRequest);
    }

    public String generateTemporaryPassword(EmailAndIdRequest emailAndIdRequest) {
        String tempPassword = TemporaryValueGenerator.generateTemporaryValue();
        userUpdater.updateTempPassword(emailAndIdRequest.getUserId(), emailAndIdRequest.getUserEmail(), tempPassword);
        return tempPassword;
    }

    public String getHiddenUserId(EmailAndNameRequest emailAndNameRequest) {
        User user = userReader.readByNameAndEmail(emailAndNameRequest.getUserName(), emailAndNameRequest.getUserEmail());
        return userProcessor.geneatePartialId(user);
    }

    public User getUserInfo(long userIdx) {
        return userFinder.findValidUser(userIdx);
    }

    public void uploadProfile(long userIdx, String profileUrl) {
        userUpdater.updateProfile(userIdx, profileUrl);
    }

    public void checkUserPassword(UserCheckPasswordRequest userCheckPasswordRequest) {
        userValidator.existsUserByPassword(userCheckPasswordRequest.getUserIdx(), userCheckPasswordRequest.getUserPassword());
    }

    public void deleteUser(long userIdx, String token) {
        userUpdater.deleteUser(userIdx, token);
    }

    public void setFirebaseToken(long userIdx, FirebaseRequest request) {
        userUpdater.updateFirebaseToken(userIdx, request.getFirebaseToken());
    }

    public void chargeCash(ChargeCashRequest chargeCashRequest) {
        cashCharger.chargeCash(chargeCashRequest.getUserId(), chargeCashRequest.getCash());
    }

    public AccessTokenResponse reCreateAccessToken(HttpServletRequest request, long userIdx) {
        User user = userFinder.findValidUser(userIdx);

        String refreshToken = jwtTokenProvider.resolveToken(request);
        tokenValidator.validateToken(refreshToken, jwtTokenProvider.getKey());
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        tokenValidator.validate(refreshToken, user);

        return AccessTokenResponse.builder()
                .accessToken(jwtTokenProvider.createAccessToken(authentication))
                .build();
    }

    public InfoValidationResponse checkNickname(String nickname) {
        boolean isExisted = userReader.existsByUserNickname(nickname);

        return InfoValidationResponse.builder()
                .status(isExisted ? 0 : 1)
                .message(isExisted ? "이미 존재하는 닉네임 입니다." : "사용할 수 있는 닉네임 입니다")
                .build();
    }

    public InfoValidationResponse checkId(String id) {
        boolean idExists = userReader.existsById(id);

        return InfoValidationResponse.builder()
                .status(idExists ? 0 : 1)
                .message(idExists ? "이미 존재하는 아이디 입니다." : "사용할 수 있는 아이디 입니다")
                .build();
    }

    public InfoValidationResponse checkEmail(String email) {
        boolean emailExists = userReader.existsByUserEmail(email);

        return InfoValidationResponse.builder()
                .status(emailExists ? 0 : 1)
                .message(emailExists ? "이미 존재하는 이메일 입니다." : "사용할 수 있는 이메일 입니다")
                .build();
    }

    public InfoValidationResponse checkPhoneNumber(String phoneNumber) {
        boolean phoneNumberExists = userReader.existsByPhoneNumber(phoneNumber);

        return InfoValidationResponse.builder()
                .status(phoneNumberExists ? 0 : 1)
                .message(phoneNumberExists ? "이미 존재하는 전화번호 입니다." : "사용할 수 있는 전화번호 입니다")
                .build();
    }

    public void patternRegister(UserPatternRequest userPatternRequest){
        userAppender.appendPattern(userPatternRequest.getUserIdx(), userPatternRequest.getPattern());
    }

    public LoginResponse loginPattern(UserPatternRequest userPatternRequest){
        User user = userFinder.findValidUser(userPatternRequest.getUserIdx());
        userValidator.checkPattern(user, userPatternRequest.getPattern());
        return authProcessor.authenticateAndGetToken(user, LoginStatus.PATTERN);
    }

    public LoginResponse loginFinger(long userIdx) {
        User user = userFinder.findValidUser(userIdx);
        return authProcessor.authenticateAndGetToken(user, LoginStatus.FINGER);
    }
}

