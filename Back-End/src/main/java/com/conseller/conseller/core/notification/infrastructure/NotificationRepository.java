package com.conseller.conseller.core.notification.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    @Query("SELECT e FROM NotificationEntity e WHERE e.notificationUser.userIdx = :userIdx ORDER BY CASE WHEN e.notificationType = 6 THEN 1 ELSE 0 END, e.notificationCreatedDate DESC")
    List<NotificationEntity> findByNotificationUser_UserIdx(Long userIdx);
}
