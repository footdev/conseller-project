package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.core.barter.api.dto.response.BarterRequestsResponse;
import com.conseller.conseller.core.barter.implement.*;
import com.conseller.conseller.core.barter.api.dto.request.BarterRegistRequest;
import com.conseller.conseller.core.barter.api.dto.response.BarterRequestResponse;
import com.conseller.conseller.core.user.domain.User;
import com.conseller.conseller.core.user.implement.UserReader;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BarterRequestService {

    private final BarterRequestReader barterRequestReader;
    private final BarterRequestAppender barterRequestAppender;
    private final BarterRequestRemover barterRequestRemover;

    private final BarterReader barterReader;
    private final BarterValidator barterValidator;

    private final BarterGuestItemAppender barterGuestItemAppender;

    private final UserReader userReader;

    public BarterRequestsResponse getBarterRequests(Long barterIdx) {
        List<BarterRequest> barterRequests = barterRequestReader.readAll(barterIdx);
        return BarterRequestsResponse.of(barterIdx, barterRequests.stream()
                .map(request -> BarterRequestResponse.of(request, request.getGifticons()))
                .collect(Collectors.toList()));
    }

    public BarterRequestResponse getBarterRequest(Long barterRequestIdx) {
        BarterRequest barterRequest = barterRequestReader.read(barterRequestIdx);
        return BarterRequestResponse.of(barterRequest, barterRequest.getGifticons());
    }

    public List<BarterRequestResponse> getBarterRequestsByUser(Long userIdx) {
        User user = userReader.read(userIdx);
        List<BarterRequest> barterRequests = barterRequestReader.readAllByUser(user.getUserIdx());

        return barterRequests.stream()
                .map(request -> BarterRequestResponse.of(request, request.getGifticons()))
                .collect(Collectors.toList());
    }

    public void addBarterRequest(BarterRegistRequest barterRegistRequest, Long barterIdx) {
        Barter barter = barterReader.read(barterIdx);
        barterValidator.isExchangeable(barter);

        User guest = userReader.read(barterRegistRequest.getUserIdx());

        BarterRequest barterRequest = BarterRequest.of(barter, guest, LocalDateTime.now());
        barterRequestAppender.append(barterRequest);

        barterGuestItemAppender.appendAll(barterRegistRequest.getBarterGuestItemList(), barterRequest.getBarterRequestIdx());
    }

    public void deleteBarterRequest(Long barterRequestIdx) {
        //물물교환 요청 조회
        BarterRequest barterRequest = barterRequestReader.read(barterRequestIdx);
        //물물교환 삭제
        barterRequestRemover.remove(barterRequest);
    }
}
