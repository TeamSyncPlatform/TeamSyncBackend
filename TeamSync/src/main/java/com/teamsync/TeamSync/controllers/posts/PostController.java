package com.teamsync.TeamSync.controllers.posts;

import com.teamsync.TeamSync.dtos.groups.channel.ChannelDTO;
import com.teamsync.TeamSync.dtos.posts.attachment.AttachmentDTO;
import com.teamsync.TeamSync.dtos.posts.comment.CommentDTO;
import com.teamsync.TeamSync.dtos.posts.post.CreatePostDTO;
import com.teamsync.TeamSync.dtos.posts.post.PostDTO;
import com.teamsync.TeamSync.dtos.posts.post.UpdatePostDTO;
import com.teamsync.TeamSync.models.groups.Group;
import com.teamsync.TeamSync.models.posts.Comment;
import com.teamsync.TeamSync.models.posts.Post;
import com.teamsync.TeamSync.models.posts.Reaction;
import com.teamsync.TeamSync.services.posts.interfaces.IAttachmentService;
import com.teamsync.TeamSync.services.posts.interfaces.IPostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/posts")
public class PostController {
    private final IPostService service;
    private final ModelMapper mapper;
    private final IAttachmentService attachmentService;

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
        post.getAttachments().forEach(attachment -> {
            attachmentService.remove(attachment.getId());
        });

        post.setAttachments(new ArrayList<>());

        Post updatedPost = service.update(mapper.map(post, Post.class));
        return mapper.map(updatedPost, PostDTO.class);
    }

    @DeleteMapping("/{postId}/physical")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostDTO> removePhysical(@PathVariable Long postId) {
        Post post = service.removePhysical(postId);
        if (post == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapper.map(post, PostDTO.class), HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostDTO> removeLogical(@PathVariable Long postId) {
        Post post = service.removeLogical(postId);
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

    @GetMapping("/{postId}/attachments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Collection<AttachmentDTO>> getPostAttachments(@PathVariable Long postId) {
        Post post = service.get(postId);
        if (post == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Collection<AttachmentDTO> attachmentsResponses = post.getAttachments().stream()
                .map(attachment -> mapper.map(attachment, AttachmentDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(attachmentsResponses, HttpStatus.OK);
    }

    @GetMapping("/{postId}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Collection<CommentDTO>> getPostComments(@PathVariable Long postId) {
        Post post = service.get(postId);
        if (post == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Collection<CommentDTO> commentResponses = post.getComments().stream()
                .map(comment -> mapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(commentResponses, HttpStatus.OK);
    }
}