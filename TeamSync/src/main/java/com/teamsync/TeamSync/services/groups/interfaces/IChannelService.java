package com.teamsync.TeamSync.services.groups.interfaces;

import com.teamsync.TeamSync.models.groups.Channel;
import com.teamsync.TeamSync.models.posts.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

public interface IChannelService {
    Collection<Channel> getAll();
    Channel get(Long channelId) throws ResponseStatusException;
    Channel create(Channel channel) throws ResponseStatusException;
    Channel update(Channel channel) throws ResponseStatusException;
    Channel removePhysical(Long channelId);
    Channel removeLogical(Long channelId);
    Boolean isNameUnique(String channelName);
}
