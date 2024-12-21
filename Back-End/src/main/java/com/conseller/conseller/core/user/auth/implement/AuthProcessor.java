package com.conseller.conseller.core.user.auth.implement;

import com.conseller.conseller.core.user.api.dto.response.LoginResponse;
import com.conseller.conseller.core.user.domain.CustomUserDetailsService;
import com.conseller.conseller.core.user.domain.User;
import com.conseller.conseller.core.user.domain.enums.LoginStatus;
import com.conseller.conseller.global.security.domain.JwtToken;
import com.conseller.conseller.global.security.infrastructure.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthProcessor {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    public Authentication getAuthentication(String userId, String userPassword, LoginStatus loginStatusType) {
        // username + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken;
        if (loginStatusType.equals(LoginStatus.GENERAL)) {
            authenticationToken = new UsernamePasswordAuthenticationToken(userId, userPassword);
        } else {
            UserDetails userDetails = customUserDetailsService.loadUserWithoutPassword(userId, userPassword);
            authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 User에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        if (!loginStatusType.equals(LoginStatus.GENERAL)) {
            return authenticationToken;
        }
        return authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    }

    public LoginResponse authenticateAndGetToken(User user, LoginStatus loginStatusType) {
        String password = loginStatusType.equals(LoginStatus.GENERAL) ? user.getUserPassword() : user.getUserPattern();
        Authentication authentication = getAuthentication(user.getUserId(), password, loginStatusType);
        JwtToken jwtToken = jwtTokenProvider.createToken(authentication);
        user.updateRefreshToken(jwtToken.getRefreshToken());

        return LoginResponse.builder()
                .userIdx(user.getUserIdx())
                .userNickname(user.getUserNickname())
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .build();
    }
}
