package com.conseller.conseller.user.api.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class EmailRequest {

    private String userEmail;
}
