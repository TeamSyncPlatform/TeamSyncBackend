package com.teamsync.TeamSync.dtos.posts.attachment;

import com.teamsync.TeamSync.dtos.posts.post.PostReference;
import com.teamsync.TeamSync.models.posts.Post;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class CreateAttachmentDTO {
    private String path;
    private PostReference post;
    private String originalName;
}
