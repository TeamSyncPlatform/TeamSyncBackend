package com.teamsync.TeamSync.services.posts.implementations;

import com.teamsync.TeamSync.models.posts.Comment;
import com.teamsync.TeamSync.models.posts.Post;
import com.teamsync.TeamSync.models.posts.Reaction;
import com.teamsync.TeamSync.repositories.posts.ICommentRepository;
import com.teamsync.TeamSync.repositories.posts.IPostRepository;
import com.teamsync.TeamSync.services.posts.interfaces.ICommentService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@Service
public class CommentService implements ICommentService {

    @Autowired
    private IPostRepository postRepository;

    @Autowired
    private ICommentRepository commentRepository;

    @Autowired
    private ICommentRepository userRepository;

    @Override
    public Collection<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Override
    public Comment get(Long commentId) throws ResponseStatusException {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
    }

    @Override
    public Comment create(Comment comment) throws ResponseStatusException {
        Post post = postRepository.findById(comment.getPost().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);

        // Update the post's comments list
        post.addComment(savedComment);
        postRepository.save(post);

        return savedComment;
    }

    @Override
    public Comment update(Comment comment) throws ResponseStatusException {
        if (!commentRepository.existsById(comment.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found");
        }
        return commentRepository.save(comment);
    }

    @Override
    public Comment remove(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        // Initialize the reactions map
        Hibernate.initialize(comment.getReactions());

        commentRepository.delete(comment);
        return comment;
    }

    @Override
    public Comment addReaction(Long commentId, Reaction reaction) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        if (!userRepository.existsById(reaction.getUserId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        comment.addReaction(reaction);
        return commentRepository.save(comment);
    }

    @Override
    public Comment removeReaction(Long commentId, Reaction reaction) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        if (!userRepository.existsById(reaction.getUserId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        comment.removeReaction(reaction);
        return commentRepository.save(comment);
    }
}