package com.conseller.conseller.core.user.api.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GifticonRequestDTO {
    private Long userIdx;
    private Integer page;
}
