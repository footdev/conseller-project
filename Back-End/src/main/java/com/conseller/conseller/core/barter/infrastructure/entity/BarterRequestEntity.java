package com.conseller.conseller.core.barter.infrastructure.entity;

import com.conseller.conseller.core.barter.api.dto.request.BarterGuestItemRequest;
import com.conseller.conseller.core.barter.api.dto.response.BarterRequestResponse;
import com.conseller.conseller.core.barter.domain.BarterRequest;
import com.conseller.conseller.core.barter.domain.enums.RequestStatus;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import com.conseller.conseller.core.user.api.dto.response.UserInfoResponse;
import com.conseller.conseller.global.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
public class BarterRequestEntity extends BaseTimeEntity {

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
    private UserEntity userEntity;

    List<BarterGuestItemEntity> barterGuestItemEntites = new ArrayList<>();

    @Column(name = "is_deleted", columnDefinition = "TINYINT(1)")
    private Boolean isDeleted;

    public BarterRequest toDomain() {
        return BarterRequest.builder()
                .barterRequestIdx(this.barterRequestIdx)
                .barterRequestStatus(RequestStatus.find(barterRequestStatus))
                .barter(this.barterEntity.toDomain())
                .user(this.userEntity.toDomain())
                .isDeleted(this.isDeleted)
                .build();
    }

    public static BarterRequestEntity of(BarterEntity barterEntity, UserEntity userEntity) {
        return BarterRequestEntity.builder()
                .barterEntity(barterEntity)
                .userEntity(userEntity)
                .build();
    }

    public static BarterRequestEntity of(BarterRequest barterRequest) {
        return BarterRequestEntity.builder()
                .barterRequestIdx(barterRequest.getBarterRequestIdx())
                .barterRequestStatus(barterRequest.getBarterRequestStatus().getStatus())
                .barterEntity(BarterEntity.of(barterRequest.getBarter()))
                .userEntity(UserEntity.of(barterRequest.getUser()))
                .isDeleted(barterRequest.getIsDeleted())
                .build();
    }

    public static List<BarterRequestEntity> of(List<BarterRequest> requests) {
        return requests.stream()
                .map(BarterRequestEntity::of)
                .collect(Collectors.toList());
    }
}
