package com.teamsync.TeamSync.dtos.posts.attachment;

import com.teamsync.TeamSync.dtos.posts.post.PostReference;
import com.teamsync.TeamSync.models.posts.Post;
import lombok.Data;

@Data
public class AttachmentDTO {
    private Long id;
    private String path;
    private PostReference post;
    private String originalName;
}
