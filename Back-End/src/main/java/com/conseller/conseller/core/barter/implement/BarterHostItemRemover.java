package com.conseller.conseller.core.barter.implement;

import com.conseller.conseller.core.barter.domain.BarterHostGifticons;
import com.conseller.conseller.core.barter.domain.Gifticons;
import com.conseller.conseller.core.barter.infrastructure.BarterHostItemRepository;
import com.conseller.conseller.core.gifticon.implement.GifticonModifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BarterHostItemRemover {

    private final BarterHostItemReader barterHostItemReader;

    public void removeAll(Long barterId) {
        Gifticons barterHostItems = BarterHostGifticons.from(barterHostItemReader.readAll(barterId));
        barterHostItems.deleteAll();
    }
}
