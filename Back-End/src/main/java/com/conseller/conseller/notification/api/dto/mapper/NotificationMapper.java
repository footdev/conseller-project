package com.conseller.conseller.notification.api.dto.mapper;

import com.conseller.conseller.entity.NotificationEntity;
import com.conseller.conseller.notification.api.dto.response.NotificationItemData;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static com.conseller.conseller.utils.DateTimeConverter.*;

@Mapper(componentModel="spring")
public interface NotificationMapper {
    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    //NotificationList -> NotificationItemDataList 매핑
    @Named("N2N")
    default NotificationItemData notificationToItemData(NotificationEntity notification) {
        NotificationItemData itemData = new NotificationItemData();

        itemData.setNotificationIdx(notification.getNotificationIdx());
        itemData.setNotificationType(notification.getNotificationType());
        itemData.setNotificationCreatedDate(convertString(notification.getNotificationCreatedDate()));
        itemData.setNotificationStatus(notification.getNotificationContent());

        return itemData;
    }

    @IterableMapping(qualifiedByName = "N2N")
    List<NotificationItemData> notificationsToItemDatas(List<NotificationEntity> notificationEntityList);
}