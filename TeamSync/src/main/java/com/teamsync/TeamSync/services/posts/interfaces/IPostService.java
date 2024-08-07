package com.teamsync.TeamSync.services.posts.interfaces;

import com.teamsync.TeamSync.models.posts.Comment;
import com.teamsync.TeamSync.models.posts.Post;
import com.teamsync.TeamSync.models.posts.Reaction;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

public interface IPostService {
    Collection<Post> getAll();
    Post get(Long postId) throws ResponseStatusException;
    Post create(Post post) throws ResponseStatusException;
    Post update(Post post) throws ResponseStatusException;
    Post remove(Long postId);
    Post addReaction(Long postId, Reaction reaction);
    Post removeReaction(Long postId, Reaction reaction);
}
