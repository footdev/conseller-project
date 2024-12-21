package com.conseller.conseller.global.api.dto;

import lombok.*;

@Builder @Getter
@AllArgsConstructor
public class EmailResponse {
    private String userName;
    private String userEmail;
    private String userPassword;
}
