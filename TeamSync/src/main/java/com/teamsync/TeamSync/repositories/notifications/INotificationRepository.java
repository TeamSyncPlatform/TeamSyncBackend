package com.teamsync.TeamSync.repositories.notifications;

import com.teamsync.TeamSync.models.notifications.Notification;
import com.teamsync.TeamSync.models.notifications.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface INotificationRepository extends JpaRepository<Notification, Long> {
    Integer countByUserIdAndIsReadFalse(@Param("userId") Long userId);
    Integer countByUserIdAndIsReadFalseAndTypeNotIn(
            @Param("userId") Long userId,
            @Param("ignoredTypes") Collection<NotificationType> ignoredTypes
    );
    Collection<Notification> findAllByUserIdAndTypeNotIn(Long userId, Collection<NotificationType> ignoredTypes);

    Collection<Notification> findAllByUserId(Long userId);
}