package com.conseller.conseller.globalApi.dto;

import lombok.*;

@Builder
@Getter @Setter @ToString
@AllArgsConstructor
public class CommonResponse {

    private Integer code;
    private String message;
}
