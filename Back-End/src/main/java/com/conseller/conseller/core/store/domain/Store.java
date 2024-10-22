package com.conseller.conseller.core.store.domain;

import com.conseller.conseller.core.gifticon.domain.Gifticon;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.store.domain.enums.StoreStatus;
import com.conseller.conseller.core.user.domain.User;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Builder
public class Store {
    private long storeIdx;
    private int storePrice;
    private LocalDateTime storeCreatedDate;
    private LocalDateTime storeEndDate;
    private LocalDateTime notificationCreatedDate;
    private String storeText;
    private String storeStatus = StoreStatus.IN_PROGRESS.getStatus();
    private Gifticon gifticon;
    private User user;
    private User consumer;
}
