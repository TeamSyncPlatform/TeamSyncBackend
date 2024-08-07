package com.teamsync.TeamSync.models.notifications;

import com.teamsync.TeamSync.models.users.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "notifications")
@TableGenerator(name="notifications_id_generator", table="primary_keys", pkColumnName="key_pk", pkColumnValue="notification", initialValue = 1, valueColumnName="value_pk")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "notifications_id_generator")
    private Long id;

    private String message;

    @Enumerated
    private NotificationType type;

    private Date creationDate;

    private Boolean isRead = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Notification(String message, NotificationType type, Date creationDate, User user) {
        this.message = message;
        this.type = type;
        this.creationDate = creationDate;
        this.user = user;
    }

    public Notification() {}


}