package com.conseller.conseller.core.barter.infrastructure.entity;

import com.conseller.conseller.core.barter.api.dto.request.BarterGuestItemRequest;
import com.conseller.conseller.core.barter.api.dto.response.BarterRequestResponse;
import com.conseller.conseller.core.barter.domain.BarterRequest;
import com.conseller.conseller.core.barter.domain.enums.RequestStatus;
import com.conseller.conseller.core.user.infrastructure.User;
import com.conseller.conseller.core.user.api.dto.response.UserInfoResponse;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "barterRequestIdx")
public class BarterRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long barterRequestIdx;

    @Column(name = "barter_request_status")
    private String barterRequestStatus = RequestStatus.WAIT.getStatus();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barter_idx", nullable = false)
    private BarterEntity barterEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx", nullable = false)
    private User user;

    @OneToMany(mappedBy = "barterRequest")
    List<BarterGuestItemEntity> barterGuestItemEntites = new ArrayList<>();

    public BarterRequest toDomain() {
        return BarterRequest.builder()
                .barterRequestIdx(barterRequestIdx)
                .barterRequestStatus(barterRequestStatus)
                .barter(barterEntity.toDomain())
                .user(user.toDomain())
                .build();
    }

    public static BarterRequestEntity of(BarterEntity barterEntity, User user) {
        return BarterRequestEntity.builder()
                .barterEntity(barterEntity)
                .user(user)
                .build();
    }

    public static BarterRequestEntity of(BarterRequest barterRequest) {
        return BarterRequestEntity.builder()
                .barterRequestIdx(barterRequest.getBarterRequestIdx())
                .barterRequestStatus(barterRequest.getBarterRequestStatus())
                .barterEntity(BarterEntity.of(barterRequest.getBarter()))
                .user(User.of(barterRequest.getUser()))
                .build();
    }

    public BarterRequestResponse toBarterRequestResponseDto(BarterRequestEntity barterRequestEntity) {
        User user = barterRequestEntity.getUser();
        UserInfoResponse userInfoResponse = UserInfoResponse.builder()
                .userId(user.getUserId())
                .userNickname(user.getUserNickname())
                .userEmail(user.getUserEmail())
                .userProfileUrl(user.getUserProfileUrl())
                .userAccount(user.getUserAccount())
                .userAccountBank(user.getUserAccountBank())
                .userPhoneNumber(user.getUserPhoneNumber())
                .build();

        List<BarterGuestItemRequest> barterGuestItemRequestList = new ArrayList<>();
        List<BarterGuestItemEntity> barterGuestItemEntityList = barterRequestEntity.getBarterGuestItemEntites();
        for(BarterGuestItemEntity bgi : barterGuestItemEntityList) {
            BarterGuestItemRequest barterGuestItemRequest = bgi.toBarterGuestItemDto(bgi);
            barterGuestItemRequestList.add(barterGuestItemRequest);
        }


        return BarterRequestResponse.builder()
                .barterRequestIdx(barterRequestEntity.getBarterRequestIdx())
                .barterRequestStatus(barterRequestEntity.getBarterRequestStatus())
                .barterIdx(barterRequestEntity.getBarterEntity().getBarterIdx())
                .barterGuestItemDtoList(barterGuestItemRequestList)
                .user(userInfoResponse)
                .build();
    }
}
