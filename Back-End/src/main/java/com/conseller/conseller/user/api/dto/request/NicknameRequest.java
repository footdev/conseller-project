package com.conseller.conseller.user.api.dto.request;

import lombok.*;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class NicknameRequest {

    private String userNickname;

}