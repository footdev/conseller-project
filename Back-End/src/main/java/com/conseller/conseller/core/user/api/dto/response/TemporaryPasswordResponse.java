package com.conseller.conseller.core.user.api.dto.response;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TemporaryPasswordResponse {

    private String temporaryPassword;

}
