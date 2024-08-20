package com.teamsync.TeamSync.repositories.users;

import com.teamsync.TeamSync.models.groups.Group;
import com.teamsync.TeamSync.models.posts.Post;
import com.teamsync.TeamSync.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> getUserByExternalIdentification(String externalIdentification);
    Optional<User> findByIdAndIsDeletedIsFalse(Long id);
    Optional<User> getUserByEmail(String email);
    @Query("SELECT u FROM User u WHERE :group NOT MEMBER OF u.groups AND u.isDeleted = false")
    List<User> findEligibleUsersForGroup(@Param("group") Group group);
}