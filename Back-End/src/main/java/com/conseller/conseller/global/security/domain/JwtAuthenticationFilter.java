package com.conseller.conseller.global.security.domain;

import com.conseller.conseller.global.security.implement.TokenValidator;
import com.conseller.conseller.global.security.infrastructure.BlackListRepository;
import com.conseller.conseller.global.security.infrastructure.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenValidator tokenValidator;
    private final BlackListRepository blackListRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Request Header 에서 JWT 토큰 추출
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

        // 2. validateToken 으로 토큰 유효성 검사
        if (token != null && tokenValidator.validateToken(token, jwtTokenProvider.getKey())) {

            //블랙리스트 토큰 확인
            if (blackListRepository.existsByAccessToken(token)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("access Token is blacklisted");
                return;
            }

            if (blackListRepository.existsByUser_RefreshToken(token)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("refresh Token is blacklisted");
                return;
            }

            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info(authentication.getName() + "'s token is valid");
        }
        filterChain.doFilter(request, response);
    }
}
