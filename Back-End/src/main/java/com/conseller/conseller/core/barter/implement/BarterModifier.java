package com.conseller.conseller.core.barter.implement;

import com.conseller.conseller.core.barter.api.dto.request.BarterModifyRequest;
import com.conseller.conseller.core.barter.domain.Barter;
import com.conseller.conseller.core.barter.infrastructure.BarterRepository;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterEntity;
import com.conseller.conseller.core.category.domain.SubCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BarterModifier {
    private final BarterRepository barterRepository;

    @Transactional
    public void modify(Barter barter, SubCategory preferSubCategory, BarterModifyRequest barterModifyRequest) {
        barter.modify(preferSubCategory, barterModifyRequest.getBarterName(), barterModifyRequest.getBarterText(), LocalDateTime.parse(barterModifyRequest.getBarterEndDate()));
        barterRepository.save(BarterEntity.of(barter));
    }
}
