package com.conseller.conseller.core.gifticon.infrastructure;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@EqualsAndHashCode(of = "usedGifticonIdx")
public class UsedGifticonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usedGifticonIdx;

    @Column(name = "used_gifticon_barcode")
    private String usedGifticonBarcode;

    @Column(name = "used_gifticon_date")
    private LocalDateTime usedGifticonDate;

    public static UsedGifticonEntity of(String usedGifticonBarcode) {
        return UsedGifticonEntity.builder()
                .usedGifticonBarcode(usedGifticonBarcode)
                .usedGifticonDate(LocalDateTime.now())
                .build();
    }
}
