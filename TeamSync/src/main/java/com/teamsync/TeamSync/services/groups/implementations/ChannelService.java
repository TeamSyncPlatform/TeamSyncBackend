package com.teamsync.TeamSync.services.groups.implementations;



import com.teamsync.TeamSync.models.groups.Channel;
import com.teamsync.TeamSync.models.groups.Group;
import com.teamsync.TeamSync.models.posts.Comment;
import com.teamsync.TeamSync.models.posts.Post;
import com.teamsync.TeamSync.repositories.groups.IChannelRepository;
import com.teamsync.TeamSync.repositories.groups.IGroupRepository;
import com.teamsync.TeamSync.services.groups.interfaces.IChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@Service
public class ChannelService implements IChannelService {
    @Autowired
    private IChannelRepository channelRepository;

    @Autowired
    private IGroupRepository groupRepository;

    @Override
    public Collection<Channel> getAll() {
        return channelRepository.findByIsDeletedFalse();
    }

    @Override
    public Channel get(Long channelId) throws ResponseStatusException {
        return getExistingChannel(channelId);
    }

    @Override
    public Channel create(Channel channel) throws ResponseStatusException {
        Group group = getExistingGroup(channel.getGroup().getId());
        channel.setGroup(group);
        Channel savedChannel = channelRepository.save(channel);
        // Update the group's channels list
        group.addChannel(savedChannel);
        groupRepository.save(group);

        return channel;
    }

    @Override
    public Channel update(Channel channel) throws ResponseStatusException {
        Channel result = getExistingChannel(channel.getId());
        return channelRepository.save(channel);
    }

    @Override
    public Channel removePhysical(Long channelId) {
        Channel channel = getExistingChannel(channelId);
        channelRepository.delete(channel);
        return channel;
    }

    @Override
    public Channel removeLogical(Long channelId) {
        Channel channel = getExistingChannel(channelId);
        channel.delete();
        return channelRepository.save(channel);
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
}