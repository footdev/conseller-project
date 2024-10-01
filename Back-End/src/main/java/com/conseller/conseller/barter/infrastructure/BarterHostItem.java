package com.conseller.conseller.barter.infrastructure;

import com.conseller.conseller.barter.api.dto.request.BarterHostItemDto;
import com.conseller.conseller.entity.Gifticon;
import com.conseller.conseller.gifticon.api.dto.response.GifticonResponse;
import lombok.*;

import javax.persistence.*;

import static com.conseller.conseller.utils.DateTimeConverter.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "barterHostItemIdx")
public class BarterHostItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long barterHostItemIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barter_idx", nullable = false)
    private Barter barter;

    @OneToOne
    @JoinColumn(name = "gifticon_idx", nullable = false)
    private Gifticon gifticon;

    @Builder
    public BarterHostItem(Barter barter, Gifticon gifticon) {
        this.barter = barter;
        this.gifticon = gifticon;
    }

    public BarterHostItemDto toBarterHostItemDto(Barter barter, GifticonResponse gifticon) {
        BarterHostItemDto barterHostItemDto = new BarterHostItemDto();
        barterHostItemDto.setBarterIdx(barter.getBarterIdx());
        barterHostItemDto.setGifticon(gifticon);

        return barterHostItemDto;
    }

    public BarterHostItemDto toBarterHostItemDto(BarterHostItem barterHostItem) {
        Gifticon gifticon = barterHostItem.getGifticon();
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

        return BarterHostItemDto.builder()
                .barterIdx(barterHostItem.getBarter().getBarterIdx())
                .gifticon(gifticonResponse)
                .build();

    }
}
