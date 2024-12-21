package com.conseller.conseller.core.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TargetUser {
    private String userId;
    private String userPassword;
}
