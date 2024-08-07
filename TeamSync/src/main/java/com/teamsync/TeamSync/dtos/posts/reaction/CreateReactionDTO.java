package com.teamsync.TeamSync.dtos.posts.reaction;

import com.teamsync.TeamSync.dtos.users.UserReference;
import com.teamsync.TeamSync.models.posts.ReactionType;
import com.teamsync.TeamSync.models.users.User;
import lombok.Data;

@Data
public class CreateReactionDTO {
    private UserReference user;
    private ReactionType type;
}
