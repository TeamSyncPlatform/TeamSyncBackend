package com.teamsync.TeamSync.repositories.groups;
import com.teamsync.TeamSync.models.groups.Channel;
import com.teamsync.TeamSync.models.groups.Group;
import com.teamsync.TeamSync.models.posts.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface IChannelRepository extends JpaRepository<Channel, Long> {
    Collection<Channel> findByIsDeletedFalse();
    Optional<Channel> findByNameAndIsDeletedFalse(String name);
    List<Channel> findAllByGroupId(Long groupId);
}