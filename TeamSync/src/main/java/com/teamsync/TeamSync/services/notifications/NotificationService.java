package com.teamsync.TeamSync.services.notifications;

import com.teamsync.TeamSync.models.notifications.Notification;
import com.teamsync.TeamSync.repositories.notifications.INotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@Service
public class NotificationService implements INotificationService {
    @Autowired
    private INotificationRepository notificationRepository;

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
        return notificationRepository.save(notification);
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
}