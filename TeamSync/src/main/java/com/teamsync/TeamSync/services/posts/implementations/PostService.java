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

import java.util.Collection;

@Service
public class PostService implements IPostService {
    @Autowired
    private IPostRepository postRepository;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public Collection<Post> getAll() {
        return postRepository.findByIsDeletedFalse();
    }

    @Override
    public Post get(Long postId) throws ResponseStatusException {
        return getExistingPost(postId);
    }

    @Override
    public Post create(Post post) throws ResponseStatusException {
        checkUserExistence(post.getAuthor().getId());
        return postRepository.save(post);
    }

    @Override
    public Post update(Post post) throws ResponseStatusException {
        Post result = getExistingPost(post.getId()); //Check existence
        return postRepository.save(post);
    }

    @Override
    public Post removePhysical(Long postId) {
        Post post = getExistingPost(postId);
        // Initialize the reactions map
        Hibernate.initialize(post.getReactions());
        postRepository.delete(post);
        return post;
    }

    @Override
    public Post removeLogical(Long postId) {
        Post post = getExistingPost(postId);
        post.delete();
        return postRepository.save(post);
    }

    @Override
    public Post addReaction(Long postId, Reaction reaction) {
        Post post = getExistingPost(postId);
        checkUserExistence(reaction.getUserId());

        post.addReaction(reaction);
        return postRepository.save(post);
    }


    @Override
    public Post removeReaction(Long postId, Reaction reaction) {
        Post post = getExistingPost(postId);
        checkUserExistence(reaction.getUserId());

        post.removeReaction(reaction);
        return postRepository.save(post);
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