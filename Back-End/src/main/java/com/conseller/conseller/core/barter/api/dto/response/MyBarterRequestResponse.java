package com.conseller.conseller.core.barter.api.dto.response;

import com.conseller.conseller.core.gifticon.api.dto.response.GifticonResponse;
import lombok.*;

import java.util.List;

@Builder
@Getter @Setter @ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MyBarterRequestResponse {

    private Long barterRequestIdx;

    private String barterRequestStatus;

    private long barterIdx;

    private String barterName;

    private String barterStatus;

    private MyBarterResponse myBarterResponse;

    private List<GifticonResponse> barterGuestItems;

}
