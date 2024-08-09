package com.teamsync.TeamSync.services.users;

import com.teamsync.TeamSync.models.users.User;
import com.teamsync.TeamSync.repositories.users.IUserRepository;
import com.teamsync.TeamSync.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    UserUtils userUtils;

    @Override
    public Collection<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User get(Long userId) throws ResponseStatusException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Override
    public User create(User user) throws ResponseStatusException {
        return userRepository.save(user);
    }

    @Override
    public User update(User user) throws ResponseStatusException {
        if (!userRepository.existsById(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return userRepository.save(user);
    }

    @Override
    public User remove(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        userRepository.delete(user);
        return user;
    }

    @Override
    public User handleLogin(){
        User loggedUser = userUtils.GetLoggedUser();
        User result = userRepository.getUserByExternalIdentification(loggedUser.getExternalIdentification());
        if(result == null){
            result = create(loggedUser);
        }
        return result;
    }
}
