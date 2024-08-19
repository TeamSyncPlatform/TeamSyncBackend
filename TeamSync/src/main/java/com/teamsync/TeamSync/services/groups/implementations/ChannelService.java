package com.teamsync.TeamSync.services.groups.implementations;



import com.teamsync.TeamSync.models.groups.Channel;
import com.teamsync.TeamSync.models.groups.Group;
import com.teamsync.TeamSync.models.posts.Comment;
import com.teamsync.TeamSync.models.posts.Post;
import com.teamsync.TeamSync.repositories.groups.IChannelRepository;
import com.teamsync.TeamSync.repositories.groups.IGroupRepository;
import com.teamsync.TeamSync.repositories.posts.IPostRepository;
import com.teamsync.TeamSync.services.groups.interfaces.IChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChannelService implements IChannelService {
    @Autowired
    private IChannelRepository channelRepository;

    @Autowired
    private IGroupRepository groupRepository;

    @Override
    public Collection<Channel> getAll() {
        return channelRepository.findByIsDeletedFalse().stream()
                .map(this::filterDeletedPosts)
                .collect(Collectors.toList());
    }

    @Override
    public Channel get(Long channelId) throws ResponseStatusException {
        Channel channel = getExistingChannel(channelId);
        return filterDeletedPosts(channel);
    }

    @Override
    public Channel create(Channel channel) throws ResponseStatusException {
        Group group = getExistingGroup(channel.getGroup().getId());
        channel.setGroup(group);
        Channel savedChannel = channelRepository.save(channel);
        // Update the group's channels list
        group.addChannel(savedChannel);
        groupRepository.save(group);

        return filterDeletedPosts(savedChannel);
    }

    @Override
    public Channel update(Channel channel) throws ResponseStatusException {
        getExistingChannel(channel.getId()); // Check existence
        return filterDeletedPosts(channelRepository.save(channel));
    }

    @Override
    public Channel removePhysical(Long channelId) {
        Channel channel = getExistingChannel(channelId);
        channelRepository.delete(channel);
        return filterDeletedPosts(channel);
    }

    @Override
    public Channel removeLogical(Long channelId) {
        Channel channel = getExistingChannel(channelId);
        channel.delete();
        return filterDeletedPosts(channelRepository.save(channel));
    }

    @Override
    public Boolean isNameUnique(String groupName) {
        Optional<Channel> channelOptional = channelRepository.findByNameAndIsDeletedFalse(groupName);
        return channelOptional.isEmpty();
    }

    private Channel getExistingChannel(Long channelId){
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Channel not found"));
        if(channel.getIsDeleted()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Channel not found");
        }
        return channel;
    }

    private Group getExistingGroup(Long groupId){
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));
        if(group.getIsDeleted()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found");
        }
        return group;
    }

    private Channel filterDeletedPosts(Channel channel) {
        Channel filteredChannel = new Channel();

        filteredChannel.setId(channel.getId());
        filteredChannel.setName(channel.getName());
        filteredChannel.setGroup(channel.getGroup());
        filteredChannel.setIsDeleted(channel.getIsDeleted());

        List<Post> filteredPosts = channel.getPosts().stream()
                .filter(post -> !post.getIsDeleted())
                .collect(Collectors.toList());

        filteredChannel.setPosts(filteredPosts);

        return filteredChannel;
    }
}