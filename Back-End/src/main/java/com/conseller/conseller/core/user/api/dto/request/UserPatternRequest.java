package com.conseller.conseller.core.user.api.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserPatternRequest {

    @NotNull
    private Long userIdx;

    @NotBlank(message = "패턴을 입력해야 합니다.")
    private String pattern;
}
