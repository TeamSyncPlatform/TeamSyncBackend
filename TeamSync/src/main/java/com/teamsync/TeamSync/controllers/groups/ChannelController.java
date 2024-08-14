package com.teamsync.TeamSync.controllers.groups;

import com.teamsync.TeamSync.dtos.groups.channel.ChannelDTO;
import com.teamsync.TeamSync.dtos.groups.channel.CreateChannelDTO;
import com.teamsync.TeamSync.dtos.groups.channel.UpdateChannelDTO;
import com.teamsync.TeamSync.dtos.posts.post.PostDTO;
import com.teamsync.TeamSync.models.groups.Channel;
import com.teamsync.TeamSync.services.groups.interfaces.IChannelService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/channels")
public class ChannelController {
    private final IChannelService service;
    private final ModelMapper mapper;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Collection<ChannelDTO>> getChannels() {
        Collection<Channel> channels = service.getAll();
        Collection<ChannelDTO> channelResponses = channels.stream()
                .map(channel -> mapper.map(channel, ChannelDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(channelResponses, HttpStatus.OK);
    }

    @GetMapping("/{channelId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ChannelDTO> get(@PathVariable Long channelId) {
        Channel channel = service.get(channelId);
        if (channel == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapper.map(channel, ChannelDTO.class), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<ChannelDTO> create(@RequestBody CreateChannelDTO channel) {
        return new ResponseEntity<>(mapper.map(service.create(mapper.map(channel, Channel.class)), ChannelDTO.class), HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ChannelDTO update(@RequestBody UpdateChannelDTO channel) {
        return mapper.map(service.update(mapper.map(channel, Channel.class)), ChannelDTO.class);
    }

    @DeleteMapping("/{channelId}/physical")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<ChannelDTO> removePhysical(@PathVariable Long channelId) {
        Channel channel = service.removePhysical(channelId);
        if (channel == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapper.map(channel, ChannelDTO.class), HttpStatus.OK);
    }

    @DeleteMapping("/{channelId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<ChannelDTO> remove(@PathVariable Long channelId) {
        Channel channel = service.removeLogical(channelId);
        if (channel == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapper.map(channel, ChannelDTO.class), HttpStatus.OK);
    }

    @GetMapping("/unique/{channelName}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> isNameUnique(@PathVariable String channelName) {
        Boolean isUnique = service.isNameUnique(channelName);
        if (isUnique == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(isUnique, HttpStatus.OK);
    }

    @GetMapping("/{channelId}/posts")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Collection<PostDTO>> getChannelPosts(@PathVariable Long channelId) {
        Channel channel = service.get(channelId);
        if (channel == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Collection<PostDTO> postResponses = channel.getPosts().stream()
                .map(post -> mapper.map(post, PostDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(postResponses, HttpStatus.OK);
    }
}