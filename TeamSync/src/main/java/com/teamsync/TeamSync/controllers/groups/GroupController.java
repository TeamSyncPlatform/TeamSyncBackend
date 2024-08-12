package com.teamsync.TeamSync.controllers.groups;

import com.teamsync.TeamSync.dtos.groups.channel.ChannelDTO;
import com.teamsync.TeamSync.dtos.groups.group.CreateGroupDTO;
import com.teamsync.TeamSync.dtos.groups.group.GroupDTO;
import com.teamsync.TeamSync.dtos.groups.group.UpdateGroupDTO;
import com.teamsync.TeamSync.dtos.users.UserDTO;
import com.teamsync.TeamSync.models.groups.Channel;
import com.teamsync.TeamSync.models.groups.Group;
import com.teamsync.TeamSync.services.groups.interfaces.IChannelService;
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
    private final IChannelService channelService;
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
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<GroupDTO> create(@RequestBody CreateGroupDTO group) {
        Group createdGroup = service.create(mapper.map(group, Group.class));
        service.addMember(createdGroup.getId(), userUtils.getLoggedUser().getExternalIdentification());
        Channel channel = new Channel();
        channel.setName("General");
        channel.setGroup(createdGroup);
        channelService.create(channel);
        return new ResponseEntity<>(mapper.map(createdGroup, GroupDTO.class), HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public GroupDTO update(@RequestBody UpdateGroupDTO group) {
        return mapper.map(service.update(mapper.map(group, Group.class)), GroupDTO.class);
    }


    @DeleteMapping("/{groupId}/physical")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<GroupDTO> removePhysical(@PathVariable Long groupId) {
        Group group = service.removePhysical(groupId);
        if (group == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapper.map(group, GroupDTO.class), HttpStatus.OK);
    }

    @DeleteMapping("/{groupId}")
    @PreAuthorize("hasRole('admin')")
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

    @GetMapping("/unique/{groupName}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> isNameUnique(@PathVariable String groupName) {
        Boolean isUnique = service.isNameUnique(groupName);
        if (isUnique == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(isUnique, HttpStatus.OK);
    }

    @GetMapping("/{groupId}/members")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Collection<UserDTO>> getGroupMembers(@PathVariable Long groupId) {
        Group group = service.get(groupId);
        if (group == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Collection<UserDTO> groupResponses = group.getMembers().values().stream()
                .map(user -> mapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(groupResponses, HttpStatus.OK);
    }
}