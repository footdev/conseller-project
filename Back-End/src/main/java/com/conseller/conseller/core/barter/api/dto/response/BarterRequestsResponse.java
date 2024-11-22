package com.conseller.conseller.core.barter.api.dto.response;

import com.conseller.conseller.core.barter.api.dto.request.BarterConfirmListOfList;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public final class BarterRequestsResponse {
    private final Long barterIdx;
    private final List<BarterRequestResponse> requests;

    public static BarterRequestsResponse of(Long barterIdx, List<BarterRequestResponse> requests) {
        return new BarterRequestsResponse(barterIdx, requests);
    }
}
