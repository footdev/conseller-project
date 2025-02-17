package com.conseller.conseller.core.barter.api.dto.response;

import com.conseller.conseller.core.gifticon.api.dto.response.GifticonResponse;
import lombok.*;

import java.util.List;

@Getter @Setter @Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MyBarterResponse {
    private Long barterIdx;
    private String barterName;
    private String barterText;
    private String barterCreatedDate;
    private String barterEndDate;
    private String barterStatus;
    private String subCategory;
    private String preferSubCategory;
    private Long barterHostIdx;
    private Long barterCompleteGuestIdx;
    private List<GifticonResponse> barterHostItems;
}
