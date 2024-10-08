package com.conseller.conseller.barter.infrastructure;

import com.conseller.conseller.barter.api.dto.request.BarterGuestItemDto;
import com.conseller.conseller.entity.Gifticon;
import com.conseller.conseller.gifticon.api.dto.response.GifticonResponse;
import lombok.*;

import javax.persistence.*;

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
                .userIdx(gifticon.getUserEntity().getUserIdx())
                .build();

        return BarterGuestItemDto.builder()
                .barterRequestIdx(barterGuestItem.getBarterRequest().getBarterRequestIdx())
                .gifticon(gifticonResponse)
                .build();
    }
}
