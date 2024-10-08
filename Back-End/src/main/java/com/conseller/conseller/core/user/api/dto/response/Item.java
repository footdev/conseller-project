package com.conseller.conseller.core.user.api.dto.response;

import lombok.*;

@Builder
@Getter @Setter @ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Item<T> {
    T items;
}
