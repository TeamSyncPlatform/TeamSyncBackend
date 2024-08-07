package com.teamsync.TeamSync.dtos.groups.channel;

import com.teamsync.TeamSync.dtos.posts.post.PostReference;
import com.teamsync.TeamSync.models.posts.Post;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChannelDTO {
    private Long id;

    private String name;

    private List<PostReference> posts;
}
