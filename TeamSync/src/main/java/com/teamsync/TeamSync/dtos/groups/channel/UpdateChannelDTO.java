package com.teamsync.TeamSync.dtos.groups.channel;

import com.teamsync.TeamSync.models.posts.Post;
import lombok.Data;

import java.util.List;

@Data
public class UpdateChannelDTO {
    private Long id;
    private String name;
    private List<Post> posts;
}
