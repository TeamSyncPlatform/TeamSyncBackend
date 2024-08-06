package com.teamsync.TeamSync.repositories.notifications;

import com.teamsync.TeamSync.models.notifications.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface INotificationRepository extends JpaRepository<Notification, Long> {

}