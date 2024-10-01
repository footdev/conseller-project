package com.conseller.conseller.barter.api.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TradeBarterRequestDTO {
    private List<Long> selectedItemIndies;
}
