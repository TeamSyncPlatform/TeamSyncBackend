package com.teamsync.TeamSync.repositories.posts;

import com.teamsync.TeamSync.models.posts.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface IPostRepository extends JpaRepository<Post, Long> {
    Collection<Post> findByIsDeletedFalse();
    Post findByIdAndIsDeletedIsFalse(Long id);

    @Query("SELECT p FROM Post p WHERE p.isDeleted = false")
    Page<Post> findAllActivePosts(Pageable pageable);
}