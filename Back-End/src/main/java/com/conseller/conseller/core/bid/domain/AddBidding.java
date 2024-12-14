package com.conseller.conseller.core.bid.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AddBidding {
    private long bidUserId;
    private int price;
    private LocalDateTime createdAt;
}
