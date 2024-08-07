package com.teamsync.TeamSync.dtos.notifications;

import com.teamsync.TeamSync.dtos.users.UserReference;
import com.teamsync.TeamSync.models.notifications.NotificationType;
import com.teamsync.TeamSync.models.users.User;
import lombok.Data;

import java.util.Date;

@Data
public class NotificationDTO {
    private Long id;
    private String message;
    private NotificationType type;
    private Date creationDate;
    private Boolean isRead;
    private UserReference user;
}