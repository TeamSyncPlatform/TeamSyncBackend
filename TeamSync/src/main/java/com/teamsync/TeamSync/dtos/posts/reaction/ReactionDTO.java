package com.teamsync.TeamSync.dtos.posts.reaction;

import com.teamsync.TeamSync.models.posts.ReactionType;
import com.teamsync.TeamSync.models.users.User;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class ReactionDTO {
    private Long id;
    private User user;
    private ReactionType type;
}
