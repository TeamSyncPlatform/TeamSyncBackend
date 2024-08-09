package com.teamsync.TeamSync.controllers.groups;

import com.teamsync.TeamSync.dtos.groups.group.CreateGroupDTO;
import com.teamsync.TeamSync.dtos.groups.group.GroupDTO;
import com.teamsync.TeamSync.dtos.groups.group.UpdateGroupDTO;
import com.teamsync.TeamSync.models.groups.Group;
import com.teamsync.TeamSync.services.groups.interfaces.IGroupService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/groups")
public class GroupController {
    private final IGroupService service;
    private final ModelMapper mapper;

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
    public ResponseEntity<GroupDTO> get(@PathVariable Long groupId) {
        Group group = service.get(groupId);
        if (group == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapper.map(group, GroupDTO.class), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GroupDTO> create(@RequestBody CreateGroupDTO group) {
        return new ResponseEntity<>(mapper.map(service.create(mapper.map(group, Group.class)), GroupDTO.class), HttpStatus.CREATED);
    }

    @PutMapping
    public GroupDTO update(@RequestBody UpdateGroupDTO group) {
        return mapper.map(service.update(mapper.map(group, Group.class)), GroupDTO.class);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<GroupDTO> remove(@PathVariable Long groupId) {
        Group group = service.remove(groupId);
        if (group == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapper.map(group, GroupDTO.class), HttpStatus.OK);
    }
}