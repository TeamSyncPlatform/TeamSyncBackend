package com.teamsync.TeamSync.services.groups.implementations;

import com.teamsync.TeamSync.models.groups.Group;
import com.teamsync.TeamSync.repositories.groups.IGroupRepository;
import com.teamsync.TeamSync.services.groups.interfaces.IGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@Service
public class GroupService implements IGroupService {
    @Autowired
    private IGroupRepository groupRepository;

    @Override
    public Collection<Group> getAll() {
        return groupRepository.findByIsDeletedFalse();
    }

    @Override
    public Group get(Long groupId) throws ResponseStatusException {
        return getExistingGroup(groupId);
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
        return group;
    }

    @Override
    public Group removeLogical(Long groupId) {
        Group group = getExistingGroup(groupId);
        group.delete();
        return groupRepository.save(group);
    }

    private Group getExistingGroup(Long groupId){
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));
        if(group.getIsDeleted()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found");
        }
        return group;
    }
}
