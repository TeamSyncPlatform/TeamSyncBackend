package com.teamsync.TeamSync.controllers.groups;

import com.teamsync.TeamSync.dtos.groups.channel.ChannelDTO;
import com.teamsync.TeamSync.dtos.groups.group.CreateGroupDTO;
import com.teamsync.TeamSync.dtos.groups.group.GroupDTO;
import com.teamsync.TeamSync.dtos.groups.group.UpdateGroupDTO;
import com.teamsync.TeamSync.models.groups.Channel;
import com.teamsync.TeamSync.models.groups.Group;
import com.teamsync.TeamSync.services.groups.interfaces.IGroupService;
import com.teamsync.TeamSync.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/groups")
public class GroupController {
    private final IGroupService service;
    private final ModelMapper mapper;
    private final UserUtils userUtils;
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Collection<GroupDTO>> getGroups() {
        Collection<Group> groups = service.getAll();
        Collection<GroupDTO> groupResponses = groups.stream()
                .map(group -> mapper.map(group, GroupDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(groupResponses, HttpStatus.OK);
    }

    @GetMapping("/{groupId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GroupDTO> get(@PathVariable Long groupId) {
        Group group = service.get(groupId);
        if (group == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapper.map(group, GroupDTO.class), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GroupDTO> create(@RequestBody CreateGroupDTO group) {
        Group createdGroup = service.create(mapper.map(group, Group.class));
        service.addMember(createdGroup.getId(), userUtils.getLoggedUser().getExternalIdentification());
        return new ResponseEntity<>(mapper.map(createdGroup, GroupDTO.class), HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public GroupDTO update(@RequestBody UpdateGroupDTO group) {
        return mapper.map(service.update(mapper.map(group, Group.class)), GroupDTO.class);
    }


    @DeleteMapping("/{groupId}/physical")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GroupDTO> removePhysical(@PathVariable Long groupId) {
        Group group = service.removePhysical(groupId);
        if (group == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapper.map(group, GroupDTO.class), HttpStatus.OK);
    }

    @DeleteMapping("/{groupId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GroupDTO> removeLogical(@PathVariable Long groupId) {
        Group group = service.removeLogical(groupId);
        if (group == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapper.map(group, GroupDTO.class), HttpStatus.OK);
    }

    @PutMapping("/{groupId}/members/{userId}")
    public ResponseEntity<Void> addMember(@PathVariable Long groupId, @PathVariable Long userId) {
        try {
            service.addMember(groupId, userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @DeleteMapping("/{groupId}/members/{userId}")
    public ResponseEntity<Void> removeMember(@PathVariable Long groupId, @PathVariable Long userId) {
        try {
            service.removeMember(groupId, userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
    }

    @GetMapping("/{groupId}/channels")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Collection<ChannelDTO>> getGroupChannels(@PathVariable Long groupId) {
        Group group = service.get(groupId);
        if (group == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Collection<ChannelDTO> groupResponses = group.getChannels().stream()
                .map(channel -> mapper.map(channel, ChannelDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(groupResponses, HttpStatus.OK);
    }
}