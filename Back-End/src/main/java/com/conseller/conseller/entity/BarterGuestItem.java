package com.conseller.conseller.entity;

import com.conseller.conseller.barter.BarterGuestItem.BarterGuestItemDto.BarterGuestItemDto;
import com.conseller.conseller.gifticon.dto.response.GifticonResponse;
import com.conseller.conseller.utils.DateTimeConverter;
import lombok.*;

import javax.persistence.*;
import com.conseller.conseller.utils.DateTimeConverter.*;

import static com.conseller.conseller.utils.DateTimeConverter.convertString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "barterGuestItemIdx")
public class BarterGuestItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long barterGuestItemIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barter_request_idx", nullable = false)
    private BarterRequest barterRequest;

    @OneToOne
    @JoinColumn(name = "gifticon_idx", nullable = false)
    private Gifticon gifticon;

    @Builder
    public BarterGuestItem(BarterRequest barterRequest, Gifticon gifticon) {
        this.barterRequest = barterRequest;
        this.gifticon = gifticon;
    }

    public BarterGuestItemDto toBarterGuestItemDto(BarterGuestItem barterGuestItem) {
        Gifticon gifticon = barterGuestItem.getGifticon();
        GifticonResponse gifticonResponse = GifticonResponse.builder()
                .gifticonIdx(gifticon.getGifticonIdx())
                .gifticonBarcode(gifticon.getGifticonBarcode())
                .gifticonName(gifticon.getGifticonName())
                .gifticonStatus(gifticon.getGifticonStatus())
                .gifticonAllImageUrl(gifticon.getGifticonAllImageUrl())
                .gifticonDataImageUrl(gifticon.getGifticonDataImageUrl())
                .gifticonStartDate(convertString(gifticon.getGifticonStartDate()))
                .gifticonEndDate(convertString(gifticon.getGifticonEndDate()))
                .mainCategoryIdx(gifticon.getMainCategory().getMainCategoryIdx())
                .subCategoryIdx(gifticon.getSubCategory().getSubCategoryIdx())
                .userIdx(gifticon.getUser().getUserIdx())
                .build();

        return BarterGuestItemDto.builder()
                .barterRequestIdx(barterGuestItem.getBarterRequest().getBarterRequestIdx())
                .gifticon(gifticonResponse)
                .build();
    }
}
