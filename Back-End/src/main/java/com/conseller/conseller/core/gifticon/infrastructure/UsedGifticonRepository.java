package com.conseller.conseller.core.gifticon.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsedGifticonRepository extends JpaRepository<UsedGifticonEntity, Long> {
    boolean existsByUsedGifticonBarcode(String usedGifticonBarcode);
}
