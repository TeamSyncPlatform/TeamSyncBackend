package com.teamsync.TeamSync.controllers.users;

import com.teamsync.TeamSync.dtos.users.CreateUserDTO;
import com.teamsync.TeamSync.dtos.users.UpdateUserDTO;
import com.teamsync.TeamSync.dtos.users.UserDTO;
import com.teamsync.TeamSync.models.users.User;
import com.teamsync.TeamSync.services.users.IUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Collection<UserDTO>> getUsers() {
        Collection<User> users = service.getAll();
        Collection<UserDTO> userResponses = users.stream()
                .map(user -> mapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(userResponses, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> get(@PathVariable Long userId) {
        User user = service.get(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapper.map(user, UserDTO.class), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody CreateUserDTO user) {
        return new ResponseEntity<>(mapper.map(service.create(mapper.map(user, User.class)), UserDTO.class), HttpStatus.CREATED);
    }

    @PutMapping
    public UserDTO update(@RequestBody UpdateUserDTO user) {
        return mapper.map(service.update(mapper.map(user, User.class)), UserDTO.class);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<UserDTO> remove(@PathVariable Long userId) {
        User user = service.remove(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapper.map(user, UserDTO.class), HttpStatus.OK);
    }
}