package com.conseller.conseller.barter.api.dto.response;

import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class BarterResponse {
    private Integer totalPages;
    private Long totalElements;
    private List<BarterItemData> items;
}