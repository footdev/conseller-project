package com.conseller.conseller.user.api.dto.request;

import lombok.*;

@Builder
@Getter @Setter @ToString
@AllArgsConstructor
public class UserCheckPasswordRequest {

    private long userIdx;
    private String userPassword;

}
