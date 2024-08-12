package com.teamsync.TeamSync.services.groups.implementations;

import com.teamsync.TeamSync.models.groups.Channel;
import com.teamsync.TeamSync.models.groups.Group;
import com.teamsync.TeamSync.models.users.User;
import com.teamsync.TeamSync.repositories.groups.IGroupRepository;
import com.teamsync.TeamSync.repositories.users.IUserRepository;
import com.teamsync.TeamSync.services.groups.interfaces.IGroupService;
import com.teamsync.TeamSync.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupService implements IGroupService {
    @Autowired
    private IGroupRepository groupRepository;

    @Autowired
    private IUserRepository userRepository;

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
    }

    public void addMember(Long groupId, String externalIdentification){
        Group group = getExistingGroup(groupId);
        User user = getExistingUser(externalIdentification);
        group.addMember(user);
        user.addGroup(group);
        groupRepository.save(group);
        userRepository.save(user);
    }

    public void removeMember(Long groupId, Long userId){
        Group group = getExistingGroup(groupId);
        User user = getExistingUser(userId);
        group.removeMember(user);
        user.removeGroup(group);
        groupRepository.save(group);
        userRepository.save(user);
    }

    @Override
    public Boolean isNameUnique(String groupName) {
        Optional<Group> groupOptional = groupRepository.findByNameAndIsDeletedFalse(groupName);

        return groupOptional.isEmpty();
    }

    private Group filterDeletedChannels(Group group) {
        Group filteredGroup = new Group();

        filteredGroup.setId(group.getId());
        filteredGroup.setName(group.getName());
        filteredGroup.setMembers(new HashMap<>(group.getMembers()));
        filteredGroup.setIsDeleted(group.getIsDeleted());

        List<Channel> filteredChannels = group.getChannels().stream()
                .filter(channel -> !channel.getIsDeleted())
                .collect(Collectors.toList());

        filteredGroup.setChannels(filteredChannels);

        return filteredGroup;
    }
}
