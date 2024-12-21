package com.conseller.conseller.core.user.api.dto.response;

import lombok.*;

@Builder
@Getter
public class ItemResponse<T> {
    T items;
}
