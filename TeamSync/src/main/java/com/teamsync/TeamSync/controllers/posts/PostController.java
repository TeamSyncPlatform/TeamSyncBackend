package com.teamsync.TeamSync.controllers.posts;

import com.teamsync.TeamSync.dtos.posts.comment.CommentDTO;
import com.teamsync.TeamSync.dtos.posts.post.CreatePostDTO;
import com.teamsync.TeamSync.dtos.posts.post.PostDTO;
import com.teamsync.TeamSync.dtos.posts.post.UpdatePostDTO;
import com.teamsync.TeamSync.models.posts.Comment;
import com.teamsync.TeamSync.models.posts.Post;
import com.teamsync.TeamSync.models.posts.Reaction;
import com.teamsync.TeamSync.services.posts.interfaces.IPostService;
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
@RequestMapping("api/v1/posts")
public class PostController {
    private final IPostService service;
    private final ModelMapper mapper;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Collection<PostDTO>> getPosts() {
        Collection<Post> posts = service.getAll();
        Collection<PostDTO> postResponses = posts.stream()
                .map(post -> mapper.map(post, PostDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(postResponses, HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostDTO> get(@PathVariable Long postId) {
        Post post = service.get(postId);
        if (post == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapper.map(post, PostDTO.class), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostDTO> create(@RequestBody CreatePostDTO post) {
        return new ResponseEntity<>(mapper.map(service.create(mapper.map(post, Post.class)), PostDTO.class), HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public PostDTO update(@RequestBody UpdatePostDTO post) {
        return mapper.map(service.update(mapper.map(post, Post.class)), PostDTO.class);
    }

    @DeleteMapping("/{postId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostDTO> remove(@PathVariable Long postId) {
        Post post = service.remove(postId);
        if (post == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapper.map(post, PostDTO.class), HttpStatus.OK);
    }

    @PostMapping("/{postId}/reactions")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostDTO> addReaction(@PathVariable Long postId, @RequestBody Reaction reaction) {
        Post post = service.addReaction(postId, reaction);
        return new ResponseEntity<>(mapper.map(post, PostDTO.class), HttpStatus.OK);
    }

    @DeleteMapping("/{postId}/reactions")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostDTO> removeReaction(@PathVariable Long postId, @RequestBody Reaction reaction) {
        Post post = service.removeReaction(postId, reaction);
        return new ResponseEntity<>(mapper.map(post, PostDTO.class), HttpStatus.OK);
    }
}