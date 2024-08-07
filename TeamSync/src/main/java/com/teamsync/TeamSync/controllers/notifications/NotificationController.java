package com.teamsync.TeamSync.controllers.notifications;

import com.teamsync.TeamSync.dtos.notifications.CreateNotificationDTO;
import com.teamsync.TeamSync.dtos.notifications.NotificationDTO;
import com.teamsync.TeamSync.dtos.notifications.UpdateNotificationDTO;
import com.teamsync.TeamSync.models.notifications.Notification;
import com.teamsync.TeamSync.services.notifications.INotificationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/notifications")
public class NotificationController {
    private final INotificationService service;
    private final ModelMapper mapper;

    @GetMapping
    public ResponseEntity<Collection<NotificationDTO>> getNotifications() {
        Collection<Notification> notifications = service.getAll();
        Collection<NotificationDTO> notificationResponses =  notifications.stream()
                .map(user -> mapper.map(user, NotificationDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(notificationResponses, HttpStatus.OK);
    }

    @GetMapping({"/{notificationId}"})
    public ResponseEntity<NotificationDTO> get(@PathVariable Long notificationId) {
        Notification notification = service.get(notificationId);
        if(notification==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>( mapper.map(notification,NotificationDTO.class), HttpStatus.OK);
    }

    @PostMapping({"/"})
    public ResponseEntity<NotificationDTO> create(@RequestBody CreateNotificationDTO notification) {
        return new ResponseEntity<>(mapper.map(service.create(mapper.map(notification, Notification.class)),NotificationDTO.class), HttpStatus.CREATED);
    }

    @PutMapping({"/"})
    public NotificationDTO update(@RequestBody UpdateNotificationDTO notification) {
        return mapper.map(service.update(mapper.map(notification, Notification.class)),NotificationDTO.class);
    }

    @DeleteMapping({"/{notificationId}"})
    public ResponseEntity<NotificationDTO> remove(@PathVariable Long notificationId) {
        Notification notification = service.remove(notificationId);
        if(notification==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>( mapper.map(notification,NotificationDTO.class), HttpStatus.OK);
    }
//
//    @GetMapping({"/user/{userId}"})
//    public ResponseEntity<Collection<NotificationDTO>> getByUserId(@PathVariable Long userId){
//        Collection<Notification> notifications = service.getByUserId(userId);
//        Collection<NotificationDTO> notificationResponses =  notifications.stream()
//                .map(accommodation -> mapper.map(accommodation, NotificationDTO.class))
//                .collect(Collectors.toList());
//        return new ResponseEntity<>(notificationResponses, HttpStatus.OK);
//    }
//
//    @GetMapping({"/user/{userId}/unread-count"})
//    public ResponseEntity<Integer> getUnreadCountByUserId(@PathVariable Long userId){
//        Integer unread = service.getUnreadCountByUserId(userId);
//        return new ResponseEntity<>(unread, HttpStatus.OK);
//    }
//
//    @PutMapping({"/{notificationId}/read"})
//    public ResponseEntity<NotificationDTO> read(@PathVariable Long notificationId) {
//        Notification result = service.read(notificationId);
//        return new ResponseEntity<>(mapper.map(result, NotificationDTO.class), HttpStatus.OK);
//    }

}