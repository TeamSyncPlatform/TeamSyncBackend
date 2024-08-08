package com.teamsync.TeamSync.services.posts.implementations;

import com.teamsync.TeamSync.models.posts.Comment;
import com.teamsync.TeamSync.models.posts.Post;
import com.teamsync.TeamSync.models.posts.Reaction;
import com.teamsync.TeamSync.repositories.posts.ICommentRepository;
import com.teamsync.TeamSync.repositories.posts.IPostRepository;
import com.teamsync.TeamSync.repositories.users.IUserRepository;
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
    private IUserRepository userRepository;

    @Override
    public Collection<Comment> getAll() {
        return commentRepository.findByIsDeletedFalse();
    }

    @Override
    public Comment get(Long commentId) throws ResponseStatusException {
        return getExistingComment(commentId);
    }

    @Override
    public Comment create(Comment comment) throws ResponseStatusException {
        Post post = getExistingPost(comment.getPost().getId());
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
    public Comment removePhysical(Long commentId) {
        Comment comment = getExistingComment(commentId);

        // Initialize the reactions map
        Hibernate.initialize(comment.getReactions());

        commentRepository.delete(comment);
        return comment;
    }

    @Override
    public Comment removeLogical(Long commentId) {
        Comment comment = getExistingComment(commentId);
        comment.delete();
        return commentRepository.save(comment);
    }

    @Override
    public Comment addReaction(Long commentId, Reaction reaction) {
        Comment comment = getExistingComment(commentId);
        checkUserExistence(reaction.getUserId());
        comment.addReaction(reaction);
        return commentRepository.save(comment);
    }

    @Override
    public Comment removeReaction(Long commentId, Reaction reaction) {
        Comment comment = getExistingComment(commentId);
        checkUserExistence(reaction.getUserId());
        comment.removeReaction(reaction);
        return commentRepository.save(comment);
    }

    private Comment getExistingComment(Long commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
        if(comment.getIsDeleted()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found");
        }
        return comment;
    }

    private Post getExistingPost(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        if(post.getIsDeleted()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return post;
    }

    private void checkUserExistence(Long userId){
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }
}