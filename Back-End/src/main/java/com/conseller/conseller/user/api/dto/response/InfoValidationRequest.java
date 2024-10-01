package com.conseller.conseller.user.api.dto.response;

import lombok.*;

@ToString
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoValidationRequest {
    private int status;
    private String message;
}
