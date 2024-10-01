package com.conseller.conseller.user.api.dto.response;

import lombok.*;

@Getter @Setter @Builder @ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class AccessTokenResponse {

    private String accessToken;

}
