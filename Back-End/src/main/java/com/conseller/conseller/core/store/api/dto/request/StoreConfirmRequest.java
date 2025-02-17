package com.conseller.conseller.core.store.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreConfirmRequest {
    private Long storeIdx;

    private Boolean confirm;
}
