package com.teamsync.TeamSync.repositories.posts;

import com.teamsync.TeamSync.models.posts.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface IPostRepository extends JpaRepository<Post, Long> {
    Collection<Post> findByIsDeletedFalse();
    Post findByIdAndIsDeletedIsFalse(Long id);

    @Query("SELECT p FROM Post p WHERE p.isDeleted = false AND p.author.id = :userId")
    Page<Post> findAllUserActivePosts(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.isDeleted = false AND p.channel.id = :channelId")
    Page<Post> findAllActivePostsByChannel(@Param("channelId") Long channelId, Pageable pageable);

    List<Post> findByCreationDateBetweenAndChannelGroupIsDeletedFalse(Date startDate, Date endDate);
    List<Post> findAllByChannelGroupIdAndCreationDateBetween(Long groupId, Date startDate, Date date);
    Long countByChannelGroupIdAndCreationDateBetween(Long id, Date startDate, Date endDate);

    Optional<Post> findTopByChannelIdOrderByIdDesc(Long id);

    Long countByChannelId(Long channelId);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.isDeleted = false AND p.channel.id = :channelId AND p.creationDate > :lastReadTimestamp")
    Long countUnreadPostsAfterDate(@Param("channelId") Long channelId, @Param("lastReadTimestamp") LocalDateTime lastReadTimestamp);

}