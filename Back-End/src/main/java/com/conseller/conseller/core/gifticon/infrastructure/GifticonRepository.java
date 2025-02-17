package com.conseller.conseller.core.gifticon.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GifticonRepository extends JpaRepository<GifticonEntity, Long> {
    Optional<GifticonEntity> findByGifticonIdx(long gifticonIdx);
    List<GifticonEntity> findAllByUserUserIdx(long userIdx);
    boolean existsBygifticonBarcode(String gifticonBarcode);
}
