package com.teamsync.TeamSync.dtos.posts.post;

import com.teamsync.TeamSync.dtos.groups.channel.ChannelReference;
import com.teamsync.TeamSync.dtos.posts.attachment.AttachmentReference;
import com.teamsync.TeamSync.dtos.posts.comment.CommentReference;
import com.teamsync.TeamSync.dtos.users.UserReference;
import com.teamsync.TeamSync.models.posts.Attachment;
import com.teamsync.TeamSync.models.posts.Comment;
import com.teamsync.TeamSync.models.posts.Reaction;
import com.teamsync.TeamSync.models.posts.ReactionType;
import com.teamsync.TeamSync.models.users.User;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class CreatePostDTO {
    private String content;
    private ChannelReference channel;
}
