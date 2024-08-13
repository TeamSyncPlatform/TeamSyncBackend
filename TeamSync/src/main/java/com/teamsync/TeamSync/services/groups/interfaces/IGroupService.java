package com.teamsync.TeamSync.services.groups.interfaces;

import com.teamsync.TeamSync.models.groups.Group;
import com.teamsync.TeamSync.models.users.User;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

public interface IGroupService {
    Collection<Group> getAll();
    Group get(Long groupId) throws ResponseStatusException;
    Group create(Group group) throws ResponseStatusException;
    Group update(Group group) throws ResponseStatusException;
    Group removePhysical(Long groupId);
    Group removeLogical(Long groupId);
    public void addMember(Long groupId, Long userId);
    public void addMember(Long groupId, String externalIdentification);
    public void removeMember(Long groupId, Long userId);
    public Boolean isNameUnique(String groupName);
    Collection<User> searchGroupMembersForDeletion (Long groupId, String searchValue);
    void setGroupOwner(Long id, String externalIdentification);
}
