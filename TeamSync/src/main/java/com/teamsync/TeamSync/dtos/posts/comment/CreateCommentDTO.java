package com.teamsync.TeamSync.dtos.posts.comment;

import com.teamsync.TeamSync.models.posts.Reaction;
import com.teamsync.TeamSync.models.users.User;
import lombok.Data;

import java.util.List;

@Data
public class CreateCommentDTO {
    private String content;
    private User author;
    private List<Reaction> reactions;
}
