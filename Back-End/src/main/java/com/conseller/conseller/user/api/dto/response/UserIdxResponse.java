package com.conseller.conseller.user.api.dto.response;

import lombok.*;

@Builder
@Getter @Setter @ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserIdxResponse {

    private long userIdx;

}