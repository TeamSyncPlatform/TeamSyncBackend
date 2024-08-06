package com.teamsync.TeamSync.dtos.notifications;

import com.teamsync.TeamSync.models.notifications.NotificationType;
import lombok.Data;

@Data
public class UpdateNotificationDTO {
    private Long id;
    private String message;
    private NotificationType type;
    private Boolean isRead;
}