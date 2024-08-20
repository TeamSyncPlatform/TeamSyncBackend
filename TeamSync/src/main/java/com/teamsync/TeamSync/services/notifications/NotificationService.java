package com.teamsync.TeamSync.services.notifications;

import com.teamsync.TeamSync.models.notifications.Notification;
import com.teamsync.TeamSync.models.users.User;
import com.teamsync.TeamSync.repositories.notifications.INotificationRepository;
import com.teamsync.TeamSync.services.users.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService {
    private final INotificationRepository notificationRepository;

    private final IUserService userService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public Collection<Notification> getAll() {
        return notificationRepository.findAll();
    }

    @Override
    public Notification get(Long notificationId) throws ResponseStatusException {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found"));
    }

    @Override
    public Notification create(Notification notification) throws ResponseStatusException {
        Notification createdNotification = notificationRepository.save(notification);
        notificationRepository.flush();
        sendNotification(createdNotification);
        return createdNotification;
    }

    @Override
    public Notification update(Notification notification) throws ResponseStatusException {
        if (!notificationRepository.existsById(notification.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found");
        }
        return notificationRepository.save(notification);
    }

    @Override
    public Notification remove(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found"));
        notificationRepository.delete(notification);
        return notification;
    }

    @Override
    public Collection<Notification> getByUserId(Long userId) {
        User user = userService.get(userId);
        if (user.getIgnoredNotifications().isEmpty()) {
            return notificationRepository.findAllByUserId(userId);
        }
        return notificationRepository.findAllByUserIdAndTypeNotIn(userId, new ArrayList<>(user.getIgnoredNotifications()));
    }

    @Override
    public Integer getUnreadCountByUserId(Long userId) {
        User user = userService.get(userId);
        if (user.getIgnoredNotifications().isEmpty()) {
            return notificationRepository.countByUserIdAndIsReadFalse(userId);
        }
        return notificationRepository.countByUserIdAndIsReadFalseAndTypeNotIn(userId, new ArrayList<>(user.getIgnoredNotifications()));
    }

    @Override
    public Notification read(Long notificationId) {
        Notification notification = get(notificationId);
        notification.setIsRead(true);
        return update(notification);
    }

    private void sendNotification(Notification notification) {
        simpMessagingTemplate.convertAndSend("/notification-publisher/" + notification.getUser().getId(), notification);
    }

}