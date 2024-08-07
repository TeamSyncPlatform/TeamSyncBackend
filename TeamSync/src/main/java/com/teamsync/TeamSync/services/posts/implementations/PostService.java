package com.teamsync.TeamSync.services.posts.implementations;

import com.teamsync.TeamSync.models.posts.Post;
import com.teamsync.TeamSync.repositories.posts.IPostRepository;
import com.teamsync.TeamSync.services.posts.interfaces.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@Service
public class PostService implements IPostService {
    @Autowired
    private IPostRepository postRepository;

    @Override
    public Collection<Post> getAll() {
        return postRepository.findAll();
    }

    @Override
    public Post get(Long postId) throws ResponseStatusException {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }

    @Override
    public Post create(Post post) throws ResponseStatusException {
        return postRepository.save(post);
    }

    @Override
    public Post update(Post post) throws ResponseStatusException {
        if (!postRepository.existsById(post.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
        return postRepository.save(post);
    }

    @Override
    public Post remove(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        postRepository.delete(post);
        return post;
    }
}