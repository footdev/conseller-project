package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.core.barter.api.dto.request.BarterConfirmPageResponseDTO;
import com.conseller.conseller.core.barter.api.dto.request.BarterCreateDto;
import com.conseller.conseller.core.barter.api.dto.request.BarterFilterDto;
import com.conseller.conseller.core.barter.api.dto.request.BarterModifyRequestDto;
import com.conseller.conseller.core.barter.api.dto.response.BarterDetailResponseDTO;
import com.conseller.conseller.core.barter.api.dto.response.BarterItemData;
import com.conseller.conseller.core.barter.api.dto.response.BarterResponse;
import com.conseller.conseller.core.barter.infrastructure.BarterEntity;


import java.util.List;

public interface BarterService {

    BarterResponse getBarterList(BarterFilterDto barterFilterDto);

    BarterDetailResponseDTO getBarter(Long barterIdx, Long userIdx);

    Long addBarter(BarterCreateDto barterCreateDto);

    void modifyBarter(Long barterIdx, BarterModifyRequestDto barterModifyRequestDto);

    void deleteBarter(Long barterIdx);

    void exchangeGifticon(Long barterIdx, Long barterRequestIdx);

    void rejectRequest(Long barterIdx, Long userIdx);

    BarterConfirmPageResponseDTO getBarterConfirmPage(Long barterIdx);

    List<BarterEntity> getExpiredBarterList();

    BarterItemData getPopularBarter();
}
