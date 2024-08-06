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
        return groupRepository.findAll();
    }

    @Override
    public Group get(Long groupId) throws ResponseStatusException {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));
    }

    @Override
    public Group create(Group group) throws ResponseStatusException {
        return groupRepository.save(group);
    }

    @Override
    public Group update(Group group) throws ResponseStatusException {
        if (!groupRepository.existsById(group.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found");
        }
        return groupRepository.save(group);
    }

    @Override
    public Group remove(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));
        groupRepository.delete(group);
        return group;
    }
}
