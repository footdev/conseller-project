package com.conseller.conseller.core.user.api.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PhoneNumberRequest {

    private String userPhoneNumber;

}
