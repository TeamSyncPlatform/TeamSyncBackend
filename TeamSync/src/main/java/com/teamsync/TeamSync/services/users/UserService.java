package com.teamsync.TeamSync.services.users;

import com.teamsync.TeamSync.models.groups.Group;
import com.teamsync.TeamSync.models.notifications.NotificationType;
import com.teamsync.TeamSync.models.users.User;
import com.teamsync.TeamSync.repositories.groups.IGroupRepository;
import com.teamsync.TeamSync.repositories.users.IUserRepository;
import com.teamsync.TeamSync.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private IGroupRepository groupRepository;


    @Override
    public Collection<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User get(Long userId) throws ResponseStatusException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Override
    public User getByExternalId(String userId) throws ResponseStatusException {
        return userRepository.getUserByExternalIdentification(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }


    @Override
    public User create(User user) throws ResponseStatusException {
        return userRepository.save(user);
    }

    @Override
    public User update(User updatedUser) throws ResponseStatusException {
        User user = userRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setAddress(updatedUser.getAddress());
        user.setPhoneNumber(updatedUser.getPhoneNumber());
        user.setDepartment(updatedUser.getDepartment());
        user.setJobTitle(updatedUser.getJobTitle());
        user.setSkills(updatedUser.getSkills());
        user.setProfileImage(updatedUser.getProfileImage());
        user.setIgnoredNotifications(updatedUser.getIgnoredNotifications());

        return userRepository.save(user);
    }

    @Override
    public User remove(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        for (Group group : user.getGroups()) {
            group.removeMember(user);
            groupRepository.save(group);
        }

        userRepository.delete(user);
        return user;
    }

    @Override
    public User handleLogin() {
        User loggedUser = userUtils.getLoggedUser();
        Optional<User> optionalUser = userRepository.getUserByExternalIdentification(loggedUser.getExternalIdentification());

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();

            String loggedUserRole = loggedUser.getRole();
            String existingUserRole = existingUser.getRole();

            if (!loggedUserRole.equals(existingUserRole)) {
                existingUser.setRole(loggedUserRole);
                existingUser = userRepository.save(existingUser);
            }

            return existingUser;
        } else {
            return create(loggedUser);
        }
    }

    @Override
    public Collection<Group> searchGroups(String userId, String searchValue) {
        Optional<User> userOptional = userRepository.getUserByExternalIdentification(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Group> userGroups = user.getGroups();
            return filterGroups(userGroups, searchValue);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Collection<User> searchEligibleUsers(Long groupId, String searchValue) {
        Group group = groupRepository.findByIdAndIsDeletedFalse(groupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));

        List<User> eligibleUsers = userRepository.findEligibleUsersForGroup(group);

        return filterUsersBySearchValue(eligibleUsers, searchValue);
    }

    @Override
    public Collection<User> searchUsersInGroup(Long groupId, String searchValue) {
        Group group = groupRepository.findByIdAndIsDeletedFalse(groupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));

        List<User> users = userRepository.findAllUsersInGroup(group);

        return filterUsersBySearchValue(users, searchValue);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.getUserByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Override
    public User toggleNotification(Long userId, NotificationType notificationType) {
        User user = get(userId);
        if (user.getIgnoredNotifications().contains(notificationType)) {
            user.getIgnoredNotifications().remove(notificationType);
        } else {
            user.getIgnoredNotifications().add(notificationType);
        }

        return update(user);
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

    private List<Group> filterGroups(List<Group> groups, String searchValue) {
        if (searchValue == null || searchValue.trim().isEmpty()) {
            return groups.stream()
                    .filter(group -> !group.getIsDeleted())
                    .collect(Collectors.toList());
        }

        return groups.stream()
                .filter(group -> group.getName().toLowerCase().contains(searchValue.toLowerCase()))
                .filter(group -> !group.getIsDeleted())
                .collect(Collectors.toList());
    }
}
