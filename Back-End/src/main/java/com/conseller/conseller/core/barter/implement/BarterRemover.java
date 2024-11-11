package com.conseller.conseller.core.barter.implement;

import com.conseller.conseller.core.barter.domain.Barter;
import com.conseller.conseller.core.barter.domain.BarterRequest;
import com.conseller.conseller.core.barter.infrastructure.BarterRepository;
import com.conseller.conseller.core.gifticon.implement.GifticonModifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class BarterRemover {

    private final BarterReader barterReader;

    private final GifticonModifier gifticonModifier;
    private final BarterHostItemRemover barterHostItemRemover;
    private final BarterRequestRemover barterRequestRemover;

    @Transactional
    public void remove(Long barterId) {
        Barter barter = barterReader.read(barterId);
        barter.remove();
        barterHostItemRemover.removeAll(barterId);
    }
}
