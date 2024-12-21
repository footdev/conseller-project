package com.conseller.conseller.core.user.implement;

import com.conseller.conseller.core.user.api.dto.request.UserInfoRequest;
import com.conseller.conseller.core.user.domain.User;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import com.conseller.conseller.core.user.infrastructure.UserRepository;
import com.conseller.conseller.global.security.implement.BlackListManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserUpdater {

    private final UserRepository userRepository;
    private final UserReader userReader;
    private final UserFinder userFinder;
    private final BlackListManager blackListManager;

    @Transactional
    public void update(long userIdx, UserInfoRequest userInfoRequest) {
        User user = userFinder.findValidUser(userIdx);
        user.updateUserInfo(userInfoRequest);
        userRepository.save(UserEntity.of(user));
    }

    @Transactional
    public void updateTempPassword(String id, String email, String tempPassword) {
        User user = userReader.readByIdAndEmail(id, email);
        user.updateTempPassword(tempPassword);
        userRepository.save(UserEntity.of(user));
    }

    @Transactional
    public void updateProfile(long userIdx, String profileUrl) {
        User user = userFinder.findValidUser(userIdx);
        user.updateProfile(profileUrl);
        userRepository.save(UserEntity.of(user));
    }

    @Transactional
    public void deleteUser(long userIdx, String token) {
        User user = userFinder.findValidUser(userIdx);
        blackListManager.addBlackList(token, userIdx);
        user.delete();
        userRepository.save(UserEntity.of(user));
    }

    public void updateFirebaseToken(long userIdx, String firebaseToken) {
        User user = userFinder.findValidUser(userIdx);
        user.updateFirebaseToken(firebaseToken);
        userRepository.save(UserEntity.of(user));
    }
}
