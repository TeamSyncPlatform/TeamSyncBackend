package com.teamsync.TeamSync.services.groups.implementations;

import com.teamsync.TeamSync.models.groups.Channel;
import com.teamsync.TeamSync.models.groups.Group;
import com.teamsync.TeamSync.models.notifications.Notification;
import com.teamsync.TeamSync.models.notifications.NotificationType;
import com.teamsync.TeamSync.models.users.User;
import com.teamsync.TeamSync.repositories.groups.IGroupRepository;
import com.teamsync.TeamSync.repositories.users.IUserRepository;
import com.teamsync.TeamSync.services.groups.interfaces.IGroupService;
import com.teamsync.TeamSync.services.notifications.INotificationService;
import com.teamsync.TeamSync.services.posts.interfaces.IUnreadPostService;
import com.teamsync.TeamSync.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GroupService implements IGroupService {
    @Autowired
    private IGroupRepository groupRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private INotificationService notificationService;

    @Autowired
    private IUnreadPostService unreadPostService;

    @Override
    public Collection<Group> getAll() {
        return groupRepository.findByIsDeletedFalse().stream()
                .map(this::filterDeletedChannels)
                .collect(Collectors.toList());
    }

    @Override
    public Group get(Long groupId) throws ResponseStatusException {
        Group group = getExistingGroup(groupId);
        return filterDeletedChannels(group);
    }

    @Override
    public Group create(Group group) throws ResponseStatusException {
        return groupRepository.save(group);
    }

    @Override
    public Group update(Group group) throws ResponseStatusException {
        Group result = getExistingGroup(group.getId());
        return groupRepository.save(group);
    }

    @Override
    public Group removePhysical(Long groupId) {
        Group group = getExistingGroup(groupId);
        groupRepository.delete(group);
        return filterDeletedChannels(group);
    }

    @Override
    public Group removeLogical(Long groupId) {
        Group group = getExistingGroup(groupId);
        group.delete();
        return filterDeletedChannels(groupRepository.save(group));
    }

    private Group getExistingGroup(Long groupId){
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));
        if(group.getIsDeleted()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found");
        }
        return group;
    }

    private User getExistingUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if(user.getIsDeleted()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return user;
    }

    private User getExistingUser(String externalIdentification){
        User user = userRepository.getUserByExternalIdentification(externalIdentification)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if(user.getIsDeleted()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return user;
    }

    public void addMember(Long groupId, Long userId){
        Group group = getExistingGroup(groupId);
        User user = getExistingUser(userId);
        group.addMember(user);
        user.addGroup(group);
        groupRepository.save(group);
        userRepository.save(user);

        String notificationMessage = String.format(
                "You have been added to the group %s",
                group.getName()
        );

        if (!isIgnoredNotification(user, NotificationType.GroupsStatus)) {
            notificationService.create(new Notification(notificationMessage, NotificationType.GroupsStatus, new Date(), user));
        }

        unreadPostService.initializeUnreadPostsForNewGroupMember(userId, groupId);
    }

    public void addMember(Long groupId, String externalIdentification) {
        Group group = getExistingGroup(groupId);
        User user = getExistingUser(externalIdentification);
        group.addMember(user);
        user.addGroup(group);
        groupRepository.save(group);
        userRepository.save(user);

        String notificationMessage = String.format(
                "You have been added to the group %s",
                group.getName()
        );

        if (!isIgnoredNotification(user, NotificationType.GroupsStatus)) {
            notificationService.create(new Notification(notificationMessage, NotificationType.GroupsStatus, new Date(), user));
        }

        unreadPostService.initializeUnreadPostsForNewGroupMember(user.getId(), groupId);
    }

    private boolean isIgnoredNotification(User user, NotificationType notificationType) {
        return user.getIgnoredNotifications().contains(notificationType);
    }

    public void removeMember(Long groupId, Long userId){
        Group group = getExistingGroup(groupId);
        User user = getExistingUser(userId);

        if (user.equals(group.getOwner())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot remove the owner of the group");
        }

        group.removeMember(user);
        user.removeGroup(group);
        groupRepository.save(group);
        userRepository.save(user);

        String notificationMessage = String.format(
                "You have been removed from the group %s",
                group.getName()
        );
        if (!isIgnoredNotification(user, NotificationType.GroupsStatus)){
            notificationService.create(new Notification(notificationMessage, NotificationType.GroupsStatus, new Date(), user));
        }

        unreadPostService.removeUnreadPostsForGroupMember(userId, groupId);
    }

    @Override
    public void removeMember(Long groupId, String externalIdentification) {
        Group group = getExistingGroup(groupId);
        User user = getExistingUser(externalIdentification);

        if (user.equals(group.getOwner())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot remove the owner of the group");
        }

        group.removeMember(user);
        user.removeGroup(group);
        groupRepository.save(group);
        userRepository.save(user);

        String notificationMessage = String.format(
                "You have been removed from the group %s",
                group.getName()
        );
        if (!isIgnoredNotification(user, NotificationType.GroupsStatus)){
            notificationService.create(new Notification(notificationMessage, NotificationType.GroupsStatus, new Date(), user));
        }

        unreadPostService.removeUnreadPostsForGroupMember(user.getId(), groupId);
    }

    @Override
    public Boolean isNameUnique(String groupName) {
        Optional<Group> groupOptional = groupRepository.findByNameAndIsDeletedFalse(groupName);

        return groupOptional.isEmpty();
    }


    @Override
    public Collection<User> searchGroupMembersForDeletion (Long groupId, String searchValue) {
        Group group = groupRepository.findByIdAndIsDeletedFalse(groupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));

        User owner = group.getOwner();

        List<User> users = group.getMembers().values().stream()
                .filter(user -> !user.equals(owner))
                .toList();

        return filterUsersBySearchValue(users, searchValue);
    }

    @Override
    public void setGroupOwner(Long groupId, String externalIdentification) {
        Group group = getExistingGroup(groupId);
        User user = getExistingUser(externalIdentification);
        group.setOwner(user);
//        user.addAsOwner(group);
        groupRepository.save(group);
//        userRepository.save(user);
    }

    private List<User> filterUsersBySearchValue(List<User> users, String searchValue) {
        if (searchValue == null || searchValue.trim().isEmpty()) {
            return users;
        }

        return users.stream()
                .filter(user -> user.getFirstName().toLowerCase().contains(searchValue.toLowerCase()) ||
                        user.getLastName().toLowerCase().contains(searchValue.toLowerCase()) ||
                        user.getEmail().toLowerCase().contains(searchValue.toLowerCase()))
                .collect(Collectors.toList());
    }

    private Group filterDeletedChannels(Group group) {
        List<Channel> filteredChannels = group.getChannels().stream()
                .filter(channel -> !channel.getIsDeleted())
                .collect(Collectors.toList());

        group.setChannels(filteredChannels);

        return group;
    }
}
