package com.conseller.conseller.gifticon.infrastructure;

import com.conseller.conseller.entity.Gifticon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GifticonRepository extends JpaRepository<Gifticon, Long> {
    Optional<Gifticon> findByGifticonIdx(long gifticonIdx);
    List<Gifticon> findAllByUserUserIdx(long userIdx);
    boolean existsBygifticonBarcode(String gifticonBarcode);
}
