package com.conseller.conseller.core.barter.implement;

import com.conseller.conseller.core.barter.domain.BarterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BarterRequestRemover {
    private final BarterRequestReader barterRequestReader;
    private final BarterGuestItemReader barterGuestItemReader;
    private final BarterGuestItemRemover barterGuestItemRemover;
    private final BarterRequestAppender barterRequestAppender;

    public void removeAll(Long barterId) {
        List<BarterRequest> barterRequests = barterRequestReader.readAll(barterId);
        barterRequests.forEach(barterRequest -> barterGuestItemRemover.removeAll(barterRequest.getBarterRequestIdx()));
        barterRequests.forEach(BarterRequest::delete);
        barterRequestAppender.appendAll(barterRequests);
    }
}
