package com.teamsync.TeamSync.controllers.posts;

import com.teamsync.TeamSync.dtos.posts.comment.CommentDTO;
import com.teamsync.TeamSync.dtos.posts.comment.CreateCommentDTO;
import com.teamsync.TeamSync.dtos.posts.comment.UpdateCommentDTO;
import com.teamsync.TeamSync.models.posts.Comment;
import com.teamsync.TeamSync.models.posts.Reaction;
import com.teamsync.TeamSync.services.posts.interfaces.ICommentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/comments")
public class CommentController {
    private final ICommentService service;
    private final ModelMapper mapper;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Collection<CommentDTO>> getComments() {
        Collection<Comment> comments = service.getAll();
        Collection<CommentDTO> commentResponses = comments.stream()
                .map(comment -> mapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(commentResponses, HttpStatus.OK);
    }

    @GetMapping("/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentDTO> get(@PathVariable Long commentId) {
        Comment comment = service.get(commentId);
        if (comment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapper.map(comment, CommentDTO.class), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentDTO> create(@RequestBody CreateCommentDTO comment) {
        return new ResponseEntity<>(mapper.map(service.create(mapper.map(comment, Comment.class)), CommentDTO.class), HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public CommentDTO update(@RequestBody UpdateCommentDTO comment) {
        return mapper.map(service.update(mapper.map(comment, Comment.class)), CommentDTO.class);
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentDTO> remove(@PathVariable Long commentId) {
        Comment comment = service.remove(commentId);
        if (comment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapper.map(comment, CommentDTO.class), HttpStatus.OK);
    }

    @PostMapping("/{commentId}/reactions")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentDTO> addReaction(@PathVariable Long commentId, @RequestBody Reaction reaction) {
        Comment comment = service.addReaction(commentId, reaction);
        return new ResponseEntity<>(mapper.map(comment, CommentDTO.class), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}/reactions")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentDTO> removeReaction(@PathVariable Long commentId, @RequestBody Reaction reaction) {
        Comment comment = service.removeReaction(commentId, reaction);
        return new ResponseEntity<>(mapper.map(comment, CommentDTO.class), HttpStatus.OK);
    }
}