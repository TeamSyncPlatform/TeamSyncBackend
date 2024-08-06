package com.teamsync.TeamSync.services.posts.implementations;

import com.teamsync.TeamSync.models.posts.Reaction;
import com.teamsync.TeamSync.repositories.posts.IReactionRepository;
import com.teamsync.TeamSync.services.posts.interfaces.IReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@Service
public class ReactionService implements IReactionService {
    @Autowired
    private IReactionRepository reactionRepository;

    @Override
    public Collection<Reaction> getAll() {
        return reactionRepository.findAll();
    }

    @Override
    public Reaction get(Long reactionId) throws ResponseStatusException {
        return reactionRepository.findById(reactionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reaction not found"));
    }

    @Override
    public Reaction create(Reaction reaction) throws ResponseStatusException {
        return reactionRepository.save(reaction);
    }

    @Override
    public Reaction update(Reaction reaction) throws ResponseStatusException {
        if (!reactionRepository.existsById(reaction.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reaction not found");
        }
        return reactionRepository.save(reaction);
    }

    @Override
    public Reaction remove(Long reactionId) {
        Reaction reaction = reactionRepository.findById(reactionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reaction not found"));
        reactionRepository.delete(reaction);
        return reaction;
    }
}