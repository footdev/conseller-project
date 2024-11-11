package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.core.barter.implement.BarterGuestItemReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BarterGuestItemRemover {
    private final BarterGuestItemReader barterGuestItemReader;

    public void removeAll(Long barterRequestId) {
        Gifticons barterGuestItems = BarterGuestGifticons.from(barterGuestItemReader.readAll(barterRequestId));
        barterGuestItems.deleteAll();
    }
}
