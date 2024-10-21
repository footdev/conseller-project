package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.core.barter.infrastructure.entity.BarterRequestEntity;

import java.util.List;

public interface BarterGuestItemService {

    Void addBarterGuestItem(List<Long> gifticonList, BarterRequestEntity barterRequestEntity);
}
