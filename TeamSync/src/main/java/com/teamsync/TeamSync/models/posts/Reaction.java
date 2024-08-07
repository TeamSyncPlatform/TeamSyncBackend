package com.teamsync.TeamSync.models.posts;

import com.teamsync.TeamSync.models.users.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Embeddable
public class Reaction {

//    @ManyToOne(fetch = FetchType.LAZY)
//    private User user;
    private long userId;
    @Enumerated
    private ReactionType type;
}
