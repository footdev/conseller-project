package com.conseller.conseller.core.barter.api.dto.request;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BarterRequestRegistDto {
    private Long userIdx;
    private List<Long> barterGuestItemList;
}
