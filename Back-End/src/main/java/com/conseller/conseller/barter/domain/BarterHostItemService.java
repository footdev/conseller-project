package com.conseller.conseller.barter.domain;

import com.conseller.conseller.barter.infrastructure.Barter;

import java.time.LocalDateTime;
import java.util.List;

public interface BarterHostItemService {
    LocalDateTime addBarterHostItem(List<Long> gifticonIdx, Barter barter);
}
