package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.core.barter.api.dto.request.BarterRequestRegistDto;
import com.conseller.conseller.core.barter.api.dto.response.BarterRequestResponseDto;

import java.util.List;

public interface BarterRequestService {

    List<BarterRequestResponseDto> getBarterRequestList();

    BarterRequestResponseDto getBarterRequest(Long barterRequestIdx);

    List<BarterRequestResponseDto> getBarterRequestListByBarterIdx(Long barterIdx);

    List<BarterRequestResponseDto> getBarterRequestListByRequester(Long userIdx);

    void addBarterRequest(BarterRequestRegistDto barterRequestRegistDto, Long barterIdx);

    void deleteBarterRequest(Long barterRequestIdx);

    void rejectByTimeBarterRequest(Long barterRequestIdx);
}
