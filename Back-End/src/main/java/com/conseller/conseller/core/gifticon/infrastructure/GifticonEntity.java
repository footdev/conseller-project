package com.conseller.conseller.core.gifticon.infrastructure;

import com.conseller.conseller.core.category.infrastructure.MainCategoryEntity;
import com.conseller.conseller.core.category.infrastructure.SubCategoryEntity;
import com.conseller.conseller.core.gifticon.api.dto.response.GifticonResponse;
import com.conseller.conseller.core.gifticon.domain.Gifticon;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.conseller.conseller.global.utils.DateTimeConverter.*;

@Entity
@Builder
@Getter @Setter @ToString
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = "gifticonIdx")
public class GifticonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long gifticonIdx;

    @Column(name = "gifticon_barcode", nullable = false)
    private String gifticonBarcode;

    @Column(name = "gifticon_name", nullable = false)
    private String gifticonName;

    @CreatedDate
    @Column(name = "gifticon_start_date", nullable = false)
    private LocalDateTime gifticonStartDate;

    @Column(name = "gifticon_end_date", nullable = false)
    private LocalDateTime gifticonEndDate;

    /*
    원본이미지 : not null
    짜른 이미지 : null
     */
    @Column(name = "gifticon_all_image_url", nullable = false)
    private String gifticonAllImageUrl;

    @Column(name = "gifticon_data_image_url")
    private String gifticonDataImageUrl;

    @Column(name = "gifticon_status", nullable = false)
    private String gifticonStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_idx")
    private SubCategoryEntity subCategoryEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_category_idx")
    private MainCategoryEntity mainCategoryEntity;

    public GifticonResponse toResponseDto() {
        return GifticonResponse.builder()
                .gifticonIdx(this.gifticonIdx)
                .gifticonBarcode(this.gifticonBarcode)
                .gifticonName(this.gifticonName)
                .gifticonAllImageUrl(this.gifticonAllImageUrl)
                .gifticonDataImageUrl(this.gifticonDataImageUrl)
                .gifticonStartDate(convertString(this.gifticonStartDate))
                .gifticonEndDate(convertString(this.gifticonEndDate))
                .gifticonStatus(this.gifticonStatus)
                .userIdx(this.userEntity.getUserIdx())
                .subCategoryIdx(this.subCategoryEntity.getSubCategoryIdx())
                .mainCategoryIdx(this.mainCategoryEntity.getMainCategoryIdx())
                .build();

    }

    public Gifticon toDomain() {
        return Gifticon.builder()
                .gifticonIdx(this.gifticonIdx)
                .gifticonBarcode(this.gifticonBarcode)
                .gifticonName(this.gifticonName)
                .gifticonStartDate(this.gifticonStartDate)
                .gifticonEndDate(this.gifticonEndDate)
                .gifticonAllImageUrl(this.gifticonAllImageUrl)
                .gifticonDataImageUrl(this.gifticonDataImageUrl)
                .gifticonStatus(this.gifticonStatus)
                .user(this.userEntity.toDomain())
                .subCategory(this.subCategoryEntity.toDomain())
                .mainCategory(this.mainCategoryEntity.toDomain())
                .build();
    }

    public static GifticonEntity of(Gifticon gifticon) {
        return GifticonEntity.builder()
                .gifticonIdx(gifticon.getGifticonIdx())
                .gifticonBarcode(gifticon.getGifticonBarcode())
                .gifticonName(gifticon.getGifticonName())
                .gifticonStartDate(gifticon.getGifticonStartDate())
                .gifticonEndDate(gifticon.getGifticonEndDate())
                .gifticonAllImageUrl(gifticon.getGifticonAllImageUrl())
                .gifticonDataImageUrl(gifticon.getGifticonDataImageUrl())
                .gifticonStatus(gifticon.getGifticonStatus())
                .userEntity(UserEntity.of(gifticon.getUser()))
                .subCategoryEntity(SubCategoryEntity.of(gifticon.getSubCategory()))
                .mainCategoryEntity(MainCategoryEntity.of(gifticon.getMainCategory()))
                .build();
    }
}
