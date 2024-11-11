package com.conseller.conseller.core.barter.api.dto.response;

import com.conseller.conseller.core.barter.domain.BarterHostItem;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

import static com.conseller.conseller.global.utils.DateTimeConverter.convertString;

@Getter
@Setter
@NoArgsConstructor
public class BarterItemResponse {
    private String gifticonDataImageName;
    private String gifticonName;
    private String gifticonEndDate;

    @Builder
    public BarterItemResponse(String gifticonDataImageName, String gifticonName, String gifticonEndDate){
        this.gifticonDataImageName = gifticonDataImageName;
        this.gifticonName = gifticonName;
        this.gifticonEndDate = gifticonEndDate;
    }

    public static BarterItemResponse of(String gifticonDataImageName, String gifticonName, String gifticonEndDate){
        return BarterItemResponse.builder()
                .gifticonDataImageName(gifticonDataImageName)
                .gifticonName(gifticonName)
                .gifticonEndDate(gifticonEndDate)
                .build();
    }

    public static List<BarterItemResponse> of(List<BarterHostItem> barterHostItems){
        return barterHostItems.stream()
                .map(
                        barterHostItem -> BarterItemResponse.of(
                                barterHostItem.getHostItemName(),
                                convertString(barterHostItem.getHostItemEndDate()),
                                barterHostItem.getHostItemDataImageUrl()
                        )
                )
                .collect(Collectors.toList());

    }
}
