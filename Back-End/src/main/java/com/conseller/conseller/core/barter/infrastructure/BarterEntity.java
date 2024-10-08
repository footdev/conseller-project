package com.conseller.conseller.core.barter.infrastructure;

import com.conseller.conseller.core.barter.api.dto.request.BarterHostItemDto;
import com.conseller.conseller.core.barter.api.dto.request.BarterModifyRequestDto;
import com.conseller.conseller.core.barter.api.dto.response.BarterItemData;
import com.conseller.conseller.core.barter.api.dto.response.BarterResponseDTO;
import com.conseller.conseller.core.barter.domain.enums.BarterStatus;
import com.conseller.conseller.core.barter.api.dto.response.BarterRequestResponseDto;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.category.infrastructure.SubCategoryEntity;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import com.conseller.conseller.core.user.api.dto.response.UserInfoResponse;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "barterIdx")
public class BarterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long barterIdx;

    @Column(name = "barter_name", nullable = false)
    private String barterName;

    @Column(name = "barter_text", nullable = false)
    private String barterText;

    @CreatedDate
    private LocalDateTime barterCreatedDate;

    @Column(name = "barter_end_date", nullable = false)
    private LocalDateTime barterEndDate;

    @LastModifiedDate
    private LocalDateTime barterModifiedDate;

    @Column(name = "barter_completed_date")
    private LocalDateTime barterCompletedDate;

 // Enum의 문자열 값을 데이터베이스에 저장
    private String barterStatus = BarterStatus.EXCHANGEABLE.getStatus();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_idx")
    private UserEntity barterHost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complete_guest_idx")
    private UserEntity barterCompleteGuest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_idx")
    private SubCategoryEntity subCategoryEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prefer_sub_catergory_idx")
    private SubCategoryEntity preferSubCategoryEntity;

    @OneToMany(mappedBy = "barter")
    List<BarterRequestEntity> barterRequestEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "barter")
    List<BarterHostItem> barterHostItemList = new ArrayList<>();

    @Builder
    public BarterEntity(String barterName, String barterText, LocalDateTime barterEndDate, UserEntity barterHost, SubCategoryEntity subCategoryEntity, SubCategoryEntity preferSubCategoryEntity) {
        this.barterName = barterName;
        this.barterText = barterText;
        this.barterCreatedDate = now();
        this.barterEndDate = barterEndDate;
        this.barterHost = barterHost;
        this.subCategoryEntity = subCategoryEntity;
        this.preferSubCategoryEntity = preferSubCategoryEntity;
    }

    public BarterResponseDTO toBarterResponseDto(BarterEntity barterEntity){
        List<BarterHostItemDto> barterHostItemDtoList= new ArrayList<>();
        for(BarterHostItem bhi : barterEntity.getBarterHostItemList()) {
            BarterHostItemDto bhiDto = bhi.toBarterHostItemDto(bhi);
            barterHostItemDtoList.add(bhiDto);
        }

        List<BarterRequestResponseDto> barterRequestResponseDtoList = new ArrayList<>();
        for(BarterRequestEntity bri : barterEntity.getBarterRequestEntityList()) {
            BarterRequestResponseDto briDto = bri.toBarterRequestResponseDto(bri);
            barterRequestResponseDtoList.add(briDto);
        }

        UserEntity host = barterEntity.getBarterHost();
        UserEntity guest = barterEntity.getBarterCompleteGuest();

        UserInfoResponse hostUserInfoResponse = UserInfoResponse.builder()
                .userId(host.getUserId())
                .userEmail(host.getUserEmail())
                .userNickname(host.getUserNickname())
                .userProfileUrl(host.getUserProfileUrl())
                .build();

        UserInfoResponse guestUserInfoResponse = null;

        if(barterEntity.getBarterCompleteGuest() != null) {
            guestUserInfoResponse = UserInfoResponse.builder()
                    .userId(guest.getUserId())
                    .userEmail(guest.getUserEmail())
                    .userNickname(guest.getUserNickname())
                    .userProfileUrl(guest.getUserProfileUrl())
                    .build();
        }

        return BarterResponseDTO.builder()
                .barterIdx(barterEntity.getBarterIdx())
                .barterName(barterEntity.getBarterName())
                .barterText(barterEntity.getBarterText())
                .barterCreatedDate(convertString(barterEntity.getBarterCreatedDate()))
                .barterEndDate(convertString(barterEntity.getBarterEndDate()))
                .subCategory(barterEntity.getSubCategoryEntity().getSubCategoryContent())
                .preferSubCategory(barterEntity.getPreferSubCategoryEntity().getSubCategoryContent())
                .barterHost(hostUserInfoResponse)
                .barterCompleteGuest(guestUserInfoResponse)
                .barterRequestResponseDtoList(barterRequestResponseDtoList)
                .barterHostItemDtoList(barterHostItemDtoList)
                .build();
    }

    public void modifyBarter(BarterModifyRequestDto barterModifyRequestDto, SubCategoryEntity preferSubCategoryEntity) {
        this.barterName = barterModifyRequestDto.getBarterName();
        this.barterText = barterModifyRequestDto.getBarterText();
        this.preferSubCategoryEntity = preferSubCategoryEntity;

    }

    public BarterItemData toBarterItemData(BarterEntity barterEntity, GifticonEntity gifticonEntity, Boolean deposit) {
        return BarterItemData.builder()
                .barterIdx(barterEntity.getBarterIdx())
                .gifticonDataImageName(gifticonEntity.getGifticonDataImageUrl())
                .gifticonName(gifticonEntity.getGifticonName())
                .gifticonEndDate(convertString(gifticonEntity.getGifticonEndDate()))
                .barterEndDate(convertString(barterEntity.getBarterEndDate()))
                .deposit(deposit)
                .preper(barterEntity.getPreferSubCategoryEntity().getSubCategoryContent())
                .barterName(barterEntity.getBarterName())
                .build();
    }
}

