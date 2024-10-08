package com.conseller.conseller.core.store.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegistStoreRequest {
    private Integer storePrice;

    private String storeText;

    private Long gifticonIdx;

    private Long userIdx;
}
