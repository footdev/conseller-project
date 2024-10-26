package com.conseller.conseller.core.barter.api.dto.response;

import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class BarterListResponse {
    private Integer totalPages;
    private Long totalElements;
    private List<BarterHostItemResponse> items;
}
