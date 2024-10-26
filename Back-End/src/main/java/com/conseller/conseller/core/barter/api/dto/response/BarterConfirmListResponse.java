package com.conseller.conseller.core.barter.api.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BarterConfirmListResponse {
    private String gifticonDataImageName;
    private String gifticonName;
    private String gifticonEndDate;

    @Builder
    public BarterConfirmListResponse(String gifticonDataImageName, String gifticonName, String gifticonEndDate){
        this.gifticonDataImageName = gifticonDataImageName;
        this.gifticonName = gifticonName;
        this.gifticonEndDate = gifticonEndDate;
    }
}
