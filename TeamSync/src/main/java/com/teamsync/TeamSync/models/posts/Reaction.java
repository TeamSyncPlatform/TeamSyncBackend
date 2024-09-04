package com.teamsync.TeamSync.models.posts;

import com.teamsync.TeamSync.models.users.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Embeddable
public class Reaction {
    private long userId;
    @Enumerated
    private ReactionType type;
}
