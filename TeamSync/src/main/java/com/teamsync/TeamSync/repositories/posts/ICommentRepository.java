package com.teamsync.TeamSync.repositories.posts;

import com.teamsync.TeamSync.models.posts.Comment;
import com.teamsync.TeamSync.models.posts.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, Long> {
    Collection<Comment> findByIsDeletedFalse();
}