package com.conseller.conseller.user.api.dto.response;

import lombok.*;

@ToString
@Getter @Setter @Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PartialHiddenUserIdResponse {

    private String userEncodeId;

}
