package com.teamsync.TeamSync.repositories.users;

import com.teamsync.TeamSync.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    User getUserByExternalIdentification(String externalIdentification);
}