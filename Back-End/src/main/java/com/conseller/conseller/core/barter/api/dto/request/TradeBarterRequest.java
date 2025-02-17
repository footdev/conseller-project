package com.conseller.conseller.core.barter.api.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TradeBarterRequest {
    private List<Long> selectedItemIndies;
}
