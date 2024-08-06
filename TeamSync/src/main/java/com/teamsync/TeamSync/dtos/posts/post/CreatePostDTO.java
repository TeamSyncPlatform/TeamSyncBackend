package com.teamsync.TeamSync.dtos.posts.post;

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
    private List<Comment> comments;
    private List<Attachment> attachments;
    private List<Reaction> reactions;
    private User author;
}
