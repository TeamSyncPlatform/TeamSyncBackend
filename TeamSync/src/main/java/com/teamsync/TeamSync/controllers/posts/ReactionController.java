package com.teamsync.TeamSync.controllers.posts;

import com.teamsync.TeamSync.dtos.posts.reaction.CreateReactionDTO;
import com.teamsync.TeamSync.dtos.posts.reaction.ReactionDTO;
import com.teamsync.TeamSync.dtos.posts.reaction.UpdateReactionDTO;
import com.teamsync.TeamSync.models.posts.Reaction;
import com.teamsync.TeamSync.services.posts.interfaces.IReactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/reactions")
public class ReactionController {
    private final IReactionService service;
    private final ModelMapper mapper;

    @GetMapping
    public ResponseEntity<Collection<ReactionDTO>> getReactions() {
        Collection<Reaction> reactions = service.getAll();
        Collection<ReactionDTO> reactionResponses = reactions.stream()
                .map(reaction -> mapper.map(reaction, ReactionDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(reactionResponses, HttpStatus.OK);
    }

    @GetMapping("/{reactionId}")
    public ResponseEntity<ReactionDTO> get(@PathVariable Long reactionId) {
        Reaction reaction = service.get(reactionId);
        if (reaction == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapper.map(reaction, ReactionDTO.class), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ReactionDTO> create(@RequestBody CreateReactionDTO reaction) {
        return new ResponseEntity<>(mapper.map(service.create(mapper.map(reaction, Reaction.class)), ReactionDTO.class), HttpStatus.CREATED);
    }

    @PutMapping
    public ReactionDTO update(@RequestBody UpdateReactionDTO reaction) {
        return mapper.map(service.update(mapper.map(reaction, Reaction.class)), ReactionDTO.class);
    }

    @DeleteMapping("/{reactionId}")
    public ResponseEntity<ReactionDTO> remove(@PathVariable Long reactionId) {
        Reaction reaction = service.remove(reactionId);
        if (reaction == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapper.map(reaction, ReactionDTO.class), HttpStatus.OK);
    }
}