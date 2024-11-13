package com.conseller.conseller.core.barter.implement;

import com.conseller.conseller.core.barter.domain.BarterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BarterRequestManager {

    private final BarterRequestAppender barterRequestAppender;

    public void rejectOtherRequests(List<BarterRequest> requests, Long acceptedRequestId) {
        requests.stream()
                .filter(request -> !request.getBarterRequestIdx().equals(acceptedRequestId))
                .forEach(BarterRequest::reject);
    }

    public void acceptAt(BarterRequest acceptedRequest) {
        acceptedRequest.accept();
        barterRequestAppender.append(acceptedRequest);
    }
}
