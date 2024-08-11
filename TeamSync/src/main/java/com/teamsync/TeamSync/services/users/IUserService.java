package com.teamsync.TeamSync.services.users;

import com.teamsync.TeamSync.models.groups.Group;
import com.teamsync.TeamSync.models.users.User;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

public interface IUserService {
    Collection<User> getAll();
    User get(Long userId) throws ResponseStatusException;
    User getByExternalId(String userId) throws ResponseStatusException;
    User create(User user) throws ResponseStatusException;
    User update(User user) throws ResponseStatusException;
    User remove(Long userId);
    User handleLogin();
    Collection<Group> searchGroups(String userId, String searchValue);
}
