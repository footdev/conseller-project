package com.conseller.conseller.user.api.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@ToString
@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "아이디를 입력해야 합니다.")
    private String userId;

    @NotBlank(message = "비밀번호를 입력해야 합니다.")
    private String userPassword;
}
