package com.teamsync.TeamSync.repositories.posts;

import com.teamsync.TeamSync.models.posts.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface IPostRepository extends JpaRepository<Post, Long> {
    Collection<Post> findByIsDeletedFalse();
    Post findByIdAndIsDeletedIsFalse(Long id);

    @Query("SELECT p FROM Post p WHERE p.isDeleted = false AND p.author.id = :userId")
    Page<Post> findAllUserActivePosts(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.isDeleted = false AND p.channel.id = :channelId")
    Page<Post> findAllActivePostsByChannel(@Param("channelId") Long channelId, Pageable pageable);

}