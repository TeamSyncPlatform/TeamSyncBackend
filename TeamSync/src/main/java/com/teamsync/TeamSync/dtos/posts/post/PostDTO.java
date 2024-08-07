package com.teamsync.TeamSync.dtos.posts.post;

import com.teamsync.TeamSync.dtos.posts.attachment.AttachmentReference;
import com.teamsync.TeamSync.dtos.posts.comment.CommentReference;
import com.teamsync.TeamSync.dtos.users.UserReference;
import com.teamsync.TeamSync.models.posts.Attachment;
import com.teamsync.TeamSync.models.posts.Comment;
import com.teamsync.TeamSync.models.posts.Reaction;
import com.teamsync.TeamSync.models.posts.ReactionType;
import com.teamsync.TeamSync.models.users.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class PostDTO {
    private Long id;
    private String content;
    private Date creationDate;
    private List<CommentReference> comments;
    private List<AttachmentReference> attachments;
    private Map<Long, ReactionType> reactions;
    private UserReference author;
}
