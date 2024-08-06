package com.teamsync.TeamSync.dtos.posts.reaction;

import com.teamsync.TeamSync.models.posts.ReactionType;
import com.teamsync.TeamSync.models.users.User;
import lombok.Data;

@Data
public class UpdateReactionDTO {
    private Long id;
    private User user;
    private ReactionType type;
}
