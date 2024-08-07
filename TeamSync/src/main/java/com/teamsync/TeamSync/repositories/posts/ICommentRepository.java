package com.teamsync.TeamSync.repositories.posts;

import com.teamsync.TeamSync.models.posts.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, Long> {

}