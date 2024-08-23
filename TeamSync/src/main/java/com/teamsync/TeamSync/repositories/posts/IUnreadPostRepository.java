package com.teamsync.TeamSync.repositories.posts;

import com.teamsync.TeamSync.models.posts.Post;
import com.teamsync.TeamSync.models.posts.UnreadPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUnreadPostRepository extends JpaRepository<UnreadPost, Long> {
    Optional<UnreadPost> findByUserIdAndChannelId(Long userId, Long channelId);

    void deleteByUserIdAndChannelId(Long userId, Long id);
}
