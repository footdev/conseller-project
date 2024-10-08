package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.core.barter.infrastructure.BarterEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface BarterHostItemService {
    LocalDateTime addBarterHostItem(List<Long> gifticonIdx, BarterEntity barterEntity);
}
