package com.conseller.conseller.barter.domain;

import com.conseller.conseller.barter.infrastructure.BarterRequest;

import java.util.List;

public interface BarterGuestItemService {

    Void addBarterGuestItem(List<Long> gifticonList, BarterRequest barterRequest);
}
