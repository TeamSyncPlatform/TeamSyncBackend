package com.teamsync.TeamSync.services.groups.implementations;



import com.teamsync.TeamSync.models.groups.Channel;
import com.teamsync.TeamSync.repositories.groups.IChannelRepository;
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

    @Override
    public Collection<Channel> getAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel get(Long channelId) throws ResponseStatusException {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Channel not found"));
    }

    @Override
    public Channel create(Channel channel) throws ResponseStatusException {
        return channelRepository.save(channel);
    }

    @Override
    public Channel update(Channel channel) throws ResponseStatusException {
        if (!channelRepository.existsById(channel.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Channel not found");
        }
        return channelRepository.save(channel);
    }

    @Override
    public Channel remove(Long channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Channel not found"));
        channelRepository.delete(channel);
        return channel;
    }
}