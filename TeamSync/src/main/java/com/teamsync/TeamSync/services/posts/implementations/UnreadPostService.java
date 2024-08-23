package com.teamsync.TeamSync.services.posts.implementations;

import com.teamsync.TeamSync.models.groups.Channel;
import com.teamsync.TeamSync.models.posts.Post;
import com.teamsync.TeamSync.models.posts.UnreadPost;
import com.teamsync.TeamSync.models.users.User;
import com.teamsync.TeamSync.repositories.groups.IChannelRepository;
import com.teamsync.TeamSync.repositories.posts.IPostRepository;
import com.teamsync.TeamSync.repositories.posts.IUnreadPostRepository;
import com.teamsync.TeamSync.repositories.users.IUserRepository;
import com.teamsync.TeamSync.services.posts.interfaces.IUnreadPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UnreadPostService implements IUnreadPostService {

    @Autowired
    private IUnreadPostRepository unreadPostRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IChannelRepository channelRepository;

    @Autowired
    private IPostRepository postRepository;

    @Override
    public void updateLastReadTimestamp(Long userId, Long channelId) {
        UnreadPost unreadPost = unreadPostRepository.findByUserIdAndChannelId(userId, channelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UnreadPost not found"));

        if (unreadPost == null) {
            unreadPost = new UnreadPost(userRepository.findByIdAndIsDeletedIsFalse(userId).orElseThrow(),
                    channelRepository.findById(channelId).orElseThrow(),
                    LocalDateTime.now());
        } else {
            unreadPost.setLastReadTimestamp(LocalDateTime.now());
        }

        unreadPostRepository.save(unreadPost);
    }

    @Override
    public Long getUnreadPostsCount(Long userId, Long channelId) {
        UnreadPost unreadPost = unreadPostRepository.findByUserIdAndChannelId(userId, channelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UnreadPost not found"));

        LocalDateTime lastReadTimestamp = unreadPost.getLastReadTimestamp();

        if (lastReadTimestamp == null) {
            return postRepository.countByChannelId(channelId);
        }

        return postRepository.countUnreadPostsAfterDate(channelId, lastReadTimestamp);
    }

    @Override
    public void initializeUnreadPostsForNewGroupMember(Long userId, Long groupId) {
        User user = userRepository.findByIdAndIsDeletedIsFalse(userId).orElseThrow();
        List<Channel> channels = channelRepository.findAllByGroupId(groupId);

        for (Channel channel : channels) {
            Optional<Post> lastPost = postRepository.findTopByChannelIdOrderByIdDesc(channel.getId());

            LocalDateTime lastReadTimestamp = lastPost
                    .map(post -> toLocalDateTime(post.getCreationDate()))
                    .orElse(null);

            UnreadPost unreadPost = new UnreadPost(user, channel, lastReadTimestamp);
            unreadPostRepository.save(unreadPost);
        }
    }

    @Override
    public void removeUnreadPostsForGroupMember(Long userId, Long groupId) {
        List<Channel> channels = channelRepository.findAllByGroupId(groupId);

        for (Channel channel : channels) {
            unreadPostRepository.deleteByUserIdAndChannelId(userId, channel.getId());
        }
    }

    @Override
    public void initializeUnreadPostsForNewChannel(Long channelId) {
        Channel channel = channelRepository.findById(channelId).orElseThrow();

        for (User user : channel.getGroup().getMembers().values()) {
            Optional<Post> lastPost = postRepository.findTopByChannelIdOrderByIdDesc(channelId);

            LocalDateTime lastReadTimestamp = lastPost
                    .map(post -> toLocalDateTime(post.getCreationDate()))
                    .orElse(null);

            UnreadPost unreadPost = new UnreadPost(user, channelRepository.findById(channelId).orElseThrow(), lastReadTimestamp);
            unreadPostRepository.save(unreadPost);
        }
    }

    private LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
