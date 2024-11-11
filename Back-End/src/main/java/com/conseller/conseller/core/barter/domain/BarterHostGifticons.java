package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.core.gifticon.domain.Gifticon;
import com.conseller.conseller.core.gifticon.infrastructure.enums.GifticonStatus;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class BarterHostGifticons implements Gifticons {

    private final List<BarterHostItem> gifticons;

    @Override
    public List<Gifticon> getGifticons() {
        return gifticons.stream()
                .map(BarterHostItem::getGifticon)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAll() {
        gifticons.forEach(BarterHostItem::delete);
    }

    public static BarterHostGifticons from(List<BarterHostItem> gifticons) {
        return new BarterHostGifticons(gifticons);
    }
}
