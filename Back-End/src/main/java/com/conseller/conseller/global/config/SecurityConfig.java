package com.conseller.conseller.global.config;

import com.conseller.conseller.global.security.domain.JwtAuthenticationFilter;
import com.conseller.conseller.global.security.infrastructure.BlackListRepository;
import com.conseller.conseller.global.security.infrastructure.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final BlackListRepository blackListRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/user").permitAll()
                .antMatchers("/dummy/**").permitAll()
                .antMatchers("/user/login").permitAll()
                .antMatchers("/user/encode/**").permitAll()
                .antMatchers("/user/id").permitAll()
                .antMatchers("/user/nickname").permitAll()
                .antMatchers("/user/email").permitAll()
                .antMatchers("/user/phone-number").permitAll()
                .antMatchers("/user/savepattern").permitAll()
                .antMatchers("/user/verifypattern").permitAll()
                .antMatchers("/user/finger/**").permitAll()
                .antMatchers("/**").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, blackListRepository), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
