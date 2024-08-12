package com.teamsync.TeamSync.services.users;

import com.teamsync.TeamSync.models.groups.Group;
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
    public User update(User user) throws ResponseStatusException {
        if (!userRepository.existsById(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
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

        return optionalUser.orElseGet(() -> create(loggedUser));
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
