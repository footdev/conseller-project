package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.core.barter.infrastructure.BarterRequestEntity;

import java.util.List;

public interface BarterGuestItemService {

    Void addBarterGuestItem(List<Long> gifticonList, BarterRequestEntity barterRequestEntity);
}
