package com.teamsync.TeamSync.services.posts.interfaces;

import com.teamsync.TeamSync.models.posts.Comment;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

public interface ICommentService {
    Collection<Comment> getAll();
    Comment get(Long commentId) throws ResponseStatusException;
    Comment create(Comment comment) throws ResponseStatusException;
    Comment update(Comment comment) throws ResponseStatusException;
    Comment remove(Long commentId);
}
