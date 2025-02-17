package com.conseller.conseller.core.barter.api.dto.response;

import lombok.*;

import java.util.List;

@Getter
@RequiredArgsConstructor
public final class BarterPagingResponse {
    private final Integer totalPages;
    private final Long totalElements;
    private final List<BarterHostItemResponse> items;

    public static BarterPagingResponse of(Integer totalPages, Long totalElements, List<BarterHostItemResponse> items) {
        return new BarterPagingResponse(totalPages, totalElements, items);
    }
}
