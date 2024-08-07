package com.teamsync.TeamSync.dtos.posts.post;

import com.teamsync.TeamSync.dtos.posts.attachment.AttachmentReference;
import com.teamsync.TeamSync.dtos.posts.comment.CommentReference;
import com.teamsync.TeamSync.dtos.posts.reaction.ReactionReference;
import com.teamsync.TeamSync.dtos.users.UserReference;
import com.teamsync.TeamSync.models.posts.Attachment;
import com.teamsync.TeamSync.models.posts.Comment;
import com.teamsync.TeamSync.models.posts.Reaction;
import com.teamsync.TeamSync.models.users.User;
import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class CreatePostDTO {
    private String content;
    private Date creationDate;
    private List<CommentReference> comments;
    private List<AttachmentReference> attachments;
    private List<ReactionReference> reactions;
    private UserReference author;
}
