package com.teamsync.TeamSync.controllers.users;

import com.teamsync.TeamSync.dtos.groups.group.GroupDTO;
import com.teamsync.TeamSync.dtos.search.GroupSearchRequest;
import com.teamsync.TeamSync.dtos.users.CreateUserDTO;
import com.teamsync.TeamSync.dtos.users.UpdateUserDTO;
import com.teamsync.TeamSync.dtos.users.UserDTO;
import com.teamsync.TeamSync.models.groups.Group;
import com.teamsync.TeamSync.models.users.User;
import com.teamsync.TeamSync.services.users.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.stream.Collectors;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final IUserService service;
    private final ModelMapper mapper;

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Collection<UserDTO>> getUsers() {
        Collection<User> users = service.getAll();
        Collection<UserDTO> userResponses = users.stream()
                .map(user -> mapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(userResponses, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> get(@PathVariable Long userId) {
        User user = service.get(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapper.map(user, UserDTO.class), HttpStatus.OK);
    }

    @GetMapping("/external-id/{externalId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> getByExternalId(@PathVariable String externalId) {
        User user = service.getByExternalId(externalId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapper.map(user, UserDTO.class), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> create(@RequestBody CreateUserDTO user) {
        return new ResponseEntity<>(mapper.map(service.create(mapper.map(user, User.class)), UserDTO.class), HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public UserDTO update(@RequestBody UpdateUserDTO user) {
        return mapper.map(service.update(mapper.map(user, User.class)), UserDTO.class);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> remove(@PathVariable Long userId) {
        User user = service.remove(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapper.map(user, UserDTO.class), HttpStatus.OK);
    }

    @PutMapping("/login")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> handleLogin() {
        return new ResponseEntity<>(mapper.map(service.handleLogin(), UserDTO.class), HttpStatus.OK);
    }

    @PutMapping("/{externalId}/groups/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Collection<GroupDTO>> search(@PathVariable String externalId,
                                                       @RequestBody GroupSearchRequest request) {
        Collection<Group> groups = service.searchGroups(externalId, request.getSearchValue());
        Collection<GroupDTO> groupResponses = groups.stream()
                .map(group -> mapper.map(group, GroupDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(groupResponses, HttpStatus.OK);
    }

}