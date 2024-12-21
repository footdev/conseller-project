package com.conseller.conseller.core.user.domain;

import com.conseller.conseller.core.user.infrastructure.UserEntity;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import com.conseller.conseller.core.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userRepository.findByUserId(userId)
                .map(this::createUserDetailsByUserId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 회원을 찾을 수 없습니다."));
    }

    public UserDetails loadUserWithoutPassword(String userId, String pattern) {
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.NOT_EXIST_USER));

        return (org.springframework.security.core.userdetails.User) org.springframework.security.core.userdetails.User.builder()
                .username(userEntity.getUsername())
                .password(pattern)
                .roles(userEntity.getRoles().toArray(new String[0]))
                .build();
    }

    // 해당하는 User의 데이터가 존재한다면 UserDetails 객체로 만들어서 return
    private UserDetails createUserDetailsByUserId(UserEntity userEntity) {
        log.info("user Info in Method createUserDetails : " + userEntity.getUserId());
        org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) org.springframework.security.core.userdetails.User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(userEntity.getRoles().toArray(new String[0]))
                .build();
        log.info("userDetails password : " + userDetails.getPassword());
        return userDetails;
    }

}
