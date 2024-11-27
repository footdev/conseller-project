package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.core.gifticon.domain.Gifticon;
import com.conseller.conseller.core.user.domain.User;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BarterGuestGifticons implements Gifticons {

    private final List<BarterGuestItem> gifticons;

    @Override
    public List<Gifticon> getGifticons() {
        return gifticons.stream()
                .map(BarterGuestItem::getGifticon)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAll() {
        gifticons.forEach(BarterGuestItem::delete);
    }

    @Override
    public void modifyUserAll(User suer) {
        gifticons.forEach(gifticon -> gifticon.modifyUser(suer));
    }

    public static BarterGuestGifticons from(List<BarterGuestItem> gifticons) {
        return new BarterGuestGifticons(gifticons);
    }
}
