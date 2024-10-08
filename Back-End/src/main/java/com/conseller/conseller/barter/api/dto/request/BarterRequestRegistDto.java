package com.conseller.conseller.barter.api.dto.request;



import com.conseller.conseller.barter.infrastructure.Barter;
import com.conseller.conseller.barter.infrastructure.BarterRequest;
import com.conseller.conseller.user.infrastructure.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BarterRequestRegistDto {
    private Long userIdx;
    private List<Long> barterGuestItemList;

    public BarterRequest toEntity(Barter barter, UserEntity userEntity) {
        return BarterRequest.builder()
                .barter(barter)
                .user(userEntity)
                .build();
    }
}
