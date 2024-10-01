package com.conseller.conseller.store.api.dto.response;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class StoreResponse {
    private long storeIdx;

    private int storePrice;

    private String storeCreatedDate;

    private String storeEndDate;

    private String storeText;

    private String storeStatus;

    private long gifticonIdx;

    private long consumerIdx;
}
