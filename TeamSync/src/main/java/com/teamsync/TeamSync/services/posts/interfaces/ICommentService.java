package com.teamsync.TeamSync.services.posts.interfaces;

import com.teamsync.TeamSync.models.posts.Comment;
import com.teamsync.TeamSync.models.posts.Post;
import com.teamsync.TeamSync.models.posts.Reaction;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

public interface ICommentService {
    Collection<Comment> getAll();
    Comment get(Long commentId) throws ResponseStatusException;
    Comment create(Comment comment) throws ResponseStatusException;
    Comment update(Comment comment) throws ResponseStatusException;
    Comment removePhysical(Long commentId);
    Comment removeLogical(Long commentId);
    Comment addReaction(Long commentId, Reaction reaction);
    Comment removeReaction(Long commentId, Reaction reaction);
    void notifyTaggedUsers(Comment createdComment);
}
