package com.conseller.conseller.core.barter.infrastructure;

import com.conseller.conseller.core.barter.api.dto.request.BarterGuestItemDto;
import com.conseller.conseller.core.barter.api.dto.response.BarterRequestResponseDto;
import com.conseller.conseller.core.barter.domain.enums.RequestStatus;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import com.conseller.conseller.core.user.api.dto.response.UserInfoResponse;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "barterRequestIdx")
public class BarterRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long barterRequestIdx;

    @Column(name = "barter_request_status")
    private String barterRequestStatus = RequestStatus.WAIT.getStatus();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barter_idx", nullable = false)
    private BarterEntity barterEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx", nullable = false)
    private UserEntity userEntity;

    @OneToMany(mappedBy = "barterRequest")
    List<BarterGuestItem> barterGuestItemList = new ArrayList<>();

    @Builder
    public BarterRequestEntity(BarterEntity barterEntity, UserEntity userEntity){
        this.barterRequestStatus = RequestStatus.WAIT.getStatus();
        this.barterEntity = barterEntity;
        this.userEntity = userEntity;
    }

    public BarterRequestResponseDto toBarterRequestResponseDto(BarterRequestEntity barterRequestEntity) {
        UserEntity userEntity = barterRequestEntity.getUserEntity();
        UserInfoResponse userInfoResponse = UserInfoResponse.builder()
                .userId(userEntity.getUserId())
                .userNickname(userEntity.getUserNickname())
                .userEmail(userEntity.getUserEmail())
                .userProfileUrl(userEntity.getUserProfileUrl())
                .userAccount(userEntity.getUserAccount())
                .userAccountBank(userEntity.getUserAccountBank())
                .userPhoneNumber(userEntity.getUserPhoneNumber())
                .build();

        List<BarterGuestItemDto> barterGuestItemDtoList  = new ArrayList<>();
        List<BarterGuestItem> barterGuestItemList = barterRequestEntity.getBarterGuestItemList();
        for(BarterGuestItem bgi : barterGuestItemList) {
            BarterGuestItemDto barterGuestItemDto = bgi.toBarterGuestItemDto(bgi);
            barterGuestItemDtoList.add(barterGuestItemDto);
        }


        return BarterRequestResponseDto.builder()
                .barterRequestIdx(barterRequestEntity.getBarterRequestIdx())
                .barterRequestStatus(barterRequestEntity.getBarterRequestStatus())
                .barterIdx(barterRequestEntity.getBarterEntity().getBarterIdx())
                .barterGuestItemDtoList(barterGuestItemDtoList)
                .user(userInfoResponse)
                .build();
    }
}
