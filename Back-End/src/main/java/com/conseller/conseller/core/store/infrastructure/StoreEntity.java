package com.conseller.conseller.core.store.infrastructure;

import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.store.domain.Store;
import com.conseller.conseller.core.store.domain.enums.StoreStatus;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "storeIdx")
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class StoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeIdx;

    @Column(name = "store_price", nullable = false)
    private Integer storePrice;

    @CreatedDate
    private LocalDateTime storeCreatedDate;

    @Column(name = "store_end_date")
    private LocalDateTime storeEndDate;

    @Column(name = "notification_created_date")
    private LocalDateTime notificationCreatedDate;

    @Column(name = "store_text")
    private String storeText;

    @Column(name = "store_status")
    private String storeStatus = StoreStatus.IN_PROGRESS.getStatus();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gifticon_idx")
    private GifticonEntity gifticonEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_user_idx")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_consumer_idx")
    private UserEntity consumerEntity;

    public Store toDomain() {
        return Store.builder()
                .storeIdx(storeIdx)
                .storePrice(storePrice)
                .storeCreatedDate(storeCreatedDate)
                .storeEndDate(storeEndDate)
                .notificationCreatedDate(notificationCreatedDate)
                .storeText(storeText)
                .storeStatus(storeStatus)
                .gifticon(gifticonEntity.toDomain())
                .user(userEntity.toDomain())
                .consumer(consumerEntity.toDomain())
                .build();
    }
}
