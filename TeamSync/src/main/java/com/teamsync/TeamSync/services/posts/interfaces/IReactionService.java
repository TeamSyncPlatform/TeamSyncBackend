package com.teamsync.TeamSync.services.posts.interfaces;

import com.teamsync.TeamSync.models.posts.Reaction;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

public interface IReactionService {
    Collection<Reaction> getAll();
    Reaction get(Long reactionId) throws ResponseStatusException;
    Reaction create(Reaction reaction) throws ResponseStatusException;
    Reaction update(Reaction reaction) throws ResponseStatusException;
    Reaction remove(Long reactionId);
}
