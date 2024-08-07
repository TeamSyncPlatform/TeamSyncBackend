package com.teamsync.TeamSync.dtos.posts.comment;

import com.teamsync.TeamSync.dtos.posts.post.PostReference;
import com.teamsync.TeamSync.dtos.users.UserReference;
import com.teamsync.TeamSync.models.posts.Post;
import com.teamsync.TeamSync.models.posts.Reaction;
import com.teamsync.TeamSync.models.posts.ReactionType;
import com.teamsync.TeamSync.models.users.User;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CreateCommentDTO {
    private String content;
    private UserReference author;
    private Map<Long, ReactionType> reactions;
    private PostReference post;
}
