package com.conseller.conseller.core.user.api.dto.response;

import lombok.*;

@ToString
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoValidationResponse {
    private int status;
    private String message;
}
