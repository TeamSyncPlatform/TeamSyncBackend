package com.teamsync.TeamSync.repositories.groups;
import com.teamsync.TeamSync.models.groups.Channel;
import com.teamsync.TeamSync.models.posts.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface IChannelRepository extends JpaRepository<Channel, Long> {
    Collection<Channel> findByIsDeletedFalse();
}