package com.teamsync.TeamSync.repositories.groups;

import com.teamsync.TeamSync.models.groups.Group;
import com.teamsync.TeamSync.models.posts.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface IGroupRepository extends JpaRepository<Group, Long> {
    Collection<Group> findByIsDeletedFalse();
    Optional<Group> findByName(String name);
    Optional<Group> findByNameAndIsDeletedFalse(String name);
    Optional<Group> findByIdAndIsDeletedFalse(Long id);
    List<Group> findAllByIsDeletedFalse();
}