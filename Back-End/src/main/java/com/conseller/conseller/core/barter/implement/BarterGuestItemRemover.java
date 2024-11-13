package com.conseller.conseller.core.barter.implement;

import com.conseller.conseller.core.barter.domain.BarterGuestGifticons;
import com.conseller.conseller.core.barter.domain.Gifticons;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BarterGuestItemRemover {
    private final BarterGuestItemReader barterGuestItemReader;

    public void removeAll(Long barterRequestId) {
        Gifticons barterGuestItems = BarterGuestGifticons.from(barterGuestItemReader.readAll(barterRequestId));
        barterGuestItems.deleteAll();
    }
}
