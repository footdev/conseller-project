package com.conseller.conseller.notification;

import com.conseller.conseller.entity.NotificationEntity;
import com.conseller.conseller.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class NotificationJdbcRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void batchInsertExpirationDateNotifications(List<NotificationEntity> notificationEntities) {

        if (notificationEntities == null || notificationEntities.isEmpty()) {
            log.error("Notification entities list is null or empty");
            throw new CustomException("기프티콘 유효기간 알림 일괄 저장에 실패했습니다: 알림 엔티티 리스트가 비어 있습니다.");
        }

        if (jdbcTemplate == null) {
            log.error("jdbcTemplate is null");
            throw new CustomException("jdbcTemplate Bean 주입에 실패했습니다.");
        }

        String sql = "INSERT INTO notification_entity (notification_content, notification_created_date, notification_title, notification_type, seller, user_idx) VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(
                sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, notificationEntities.get(i).getNotificationContent());
                        ps.setTimestamp(2, Timestamp.valueOf(notificationEntities.get(i).getNotificationCreatedDate()));
                        ps.setString(3, notificationEntities.get(i).getNotificationTitle());
                        ps.setInt(4, notificationEntities.get(i).getNotificationType());
                        ps.setBoolean(5, notificationEntities.get(i).isNotificationSeller());
                        ps.setLong(6, notificationEntities.get(i).getNotificationUser().getUserIdx());
                    }

                    @Override
                    public int getBatchSize() {
                        return notificationEntities.size();
                    }
                }
        );
    }
}
