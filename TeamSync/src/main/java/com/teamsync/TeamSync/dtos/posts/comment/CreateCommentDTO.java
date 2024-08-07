package com.teamsync.TeamSync.dtos.posts.comment;

import com.teamsync.TeamSync.dtos.posts.reaction.ReactionReference;
import com.teamsync.TeamSync.dtos.users.UserReference;
import com.teamsync.TeamSync.models.posts.Reaction;
import com.teamsync.TeamSync.models.users.User;
import lombok.Data;

import java.util.List;

@Data
public class CreateCommentDTO {
    private String content;
    private UserReference author;
    private List<ReactionReference> reactions;
}
