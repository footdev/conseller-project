package com.conseller.conseller.core.auction.api.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuctionCategoryResponse {
    private List<Integer> items;
}
