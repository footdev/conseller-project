package com.conseller.conseller.core.store.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

    @Query("select s from Store s where s.consumer.userIdx = :consumerIdx")
    List<StoreEntity> findStoresByConsumerIdx(long consumerIdx);

    @Query("select s from Store s where s.storeStatus = '거래 중' and current_timestamp - s.notificationCreatedDate >= 14 * 60 * 1000")
    List<StoreEntity> findByStoreListConfirm();

    @Query("select s from Store s where s.storeStatus != '낙찰' and s.storeEndDate <= current_timestamp ")
    List<StoreEntity> findStoreAllExpired();

    @Query("select s from Store s where s.storeStatus = '낙찰'")
    List<StoreEntity> findAwardedStoreList();
}
