package com.teamsync.TeamSync.dtos.notifications;


import com.teamsync.TeamSync.models.notifications.NotificationType;
import lombok.Data;

import java.util.Date;

@Data
public class CreateNotificationDTO {
    private String message;
    private NotificationType type;
    private Date creationDate;
    private Long userId;
}