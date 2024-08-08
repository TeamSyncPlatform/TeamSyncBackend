package com.teamsync.TeamSync.services.posts.implementations;

import com.teamsync.TeamSync.models.posts.Comment;
import com.teamsync.TeamSync.models.posts.Post;
import com.teamsync.TeamSync.models.posts.Reaction;
import com.teamsync.TeamSync.repositories.posts.IPostRepository;
import com.teamsync.TeamSync.repositories.users.IUserRepository;
import com.teamsync.TeamSync.services.posts.interfaces.IPostService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService implements IPostService {
    @Autowired
    private IPostRepository postRepository;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public Collection<Post> getAll() {
        Collection<Post> posts = postRepository.findByIsDeletedFalse();
        return posts.stream()
                .map(this::filterDeletedComments)
                .collect(Collectors.toList());
    }

    @Override
    public Post get(Long postId) throws ResponseStatusException {
        Post post = getExistingPost(postId);
        return filterDeletedComments(post);
    }

    @Override
    public Post create(Post post) throws ResponseStatusException {
        checkUserExistence(post.getAuthor().getId());
        return filterDeletedComments(postRepository.save(post));
    }

    @Override
    public Post update(Post post) throws ResponseStatusException {
        getExistingPost(post.getId()); // Check existence
        return filterDeletedComments(postRepository.save(post));
    }

    @Override
    public Post removePhysical(Long postId) {
        Post post = getExistingPost(postId);
        Hibernate.initialize(post.getReactions());
        postRepository.delete(post);
        return filterDeletedComments(post);
    }

    @Override
    public Post removeLogical(Long postId) {
        Post post = getExistingPost(postId);
        post.delete();
        return filterDeletedComments(postRepository.save(post));
    }

    @Override
    public Post addReaction(Long postId, Reaction reaction) {
        Post post = getExistingPost(postId);
        checkUserExistence(reaction.getUserId());
        post.addReaction(reaction);
        return filterDeletedComments(postRepository.save(post));
    }

    @Override
    public Post removeReaction(Long postId, Reaction reaction) {
        Post post = getExistingPost(postId);
        checkUserExistence(reaction.getUserId());
        post.removeReaction(reaction);
        return filterDeletedComments(postRepository.save(post));
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

    private Post filterDeletedComments(Post post) {
        Post filteredPost = new Post();

        filteredPost.setId(post.getId());
        filteredPost.setContent(post.getContent());
        filteredPost.setCreationDate(post.getCreationDate());
        filteredPost.setAuthor(post.getAuthor());
        filteredPost.setReactions(new HashMap<>(post.getReactions()));
        filteredPost.setAttachments(new ArrayList<>(post.getAttachments()));
        filteredPost.setChannel(post.getChannel());

        List<Comment> filteredComments = post.getComments().stream()
                .filter(comment -> !comment.getIsDeleted())
                .collect(Collectors.toList());

        filteredPost.setComments(filteredComments);

        filteredPost.setIsDeleted(post.getIsDeleted());

        return filteredPost;
    }
}