package com.teamsync.TeamSync.services.posts.implementations;

import com.teamsync.TeamSync.models.groups.Channel;
import com.teamsync.TeamSync.models.notifications.Notification;
import com.teamsync.TeamSync.models.notifications.NotificationType;
import com.teamsync.TeamSync.models.posts.Comment;
import com.teamsync.TeamSync.models.posts.Post;
import com.teamsync.TeamSync.models.posts.Reaction;
import com.teamsync.TeamSync.models.users.User;
import com.teamsync.TeamSync.repositories.groups.IChannelRepository;
import com.teamsync.TeamSync.repositories.posts.IPostRepository;
import com.teamsync.TeamSync.repositories.users.IUserRepository;
import com.teamsync.TeamSync.services.notifications.INotificationService;
import com.teamsync.TeamSync.services.posts.interfaces.IPostService;
import com.teamsync.TeamSync.utils.UserUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService implements IPostService {
    @Autowired
    private IPostRepository postRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IChannelRepository channelRepository;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private INotificationService notificationService;

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
        String externalId = userUtils.getLoggedUser().getExternalIdentification();
        User user = getExistingUser(externalId);
        Channel channel = getExistingChannel(post.getChannel().getId());

        post.setAuthor(user);
        post.setChannel(channel);
        post.setCreationDate(new Date());

//        channel.addPost(post);
//        channelRepository.save(channel);

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
        User user = userRepository.findByIdAndIsDeletedIsFalse(reaction.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        post.addReaction(reaction);

        String notificationMessage = String.format(
                "%s#%s  -  User %s %s has reacted on your post with %s",
                post.getChannel().getGroup().getName(),
                post.getChannel().getName(),
                user.getFirstName(),
                user.getLastName(),
                reaction.getType()
        );

        if(reaction.getUserId() != post.getAuthor().getId()){
            notificationService.create(new Notification(notificationMessage, NotificationType.Reaction, new Date(), post.getAuthor()));
        }

        return filterDeletedComments(postRepository.save(post));
    }

    @Override
    public Post removeReaction(Long postId, Reaction reaction) {
        Post post = getExistingPost(postId);
        User user = userRepository.findByIdAndIsDeletedIsFalse(reaction.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        post.removeReaction(reaction);
        return filterDeletedComments(postRepository.save(post));
    }

    private Post getExistingPost(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        if(post.getIsDeleted()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
        return post;
    }

    private Channel getExistingChannel(Long channelId){
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Channel not found"));
        if(channel.getIsDeleted()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Channel not found");
        }
        return channel;
    }

    private void checkUserExistence(Long userId){
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    private User getExistingUser(String externalId){
        User user = userRepository.getUserByExternalIdentification(externalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if(user.getIsDeleted()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return user;
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

    @Override
    public Page<Post> getUserPosts(Long userId, Pageable pageable) {
        return postRepository.findAllUserActivePosts(userId, pageable);
    }

    @Override
    public Page<Post> getChannelPosts(Long channelId, Pageable pageable) {
        return postRepository.findAllActivePostsByChannel(channelId, pageable);
    }
}