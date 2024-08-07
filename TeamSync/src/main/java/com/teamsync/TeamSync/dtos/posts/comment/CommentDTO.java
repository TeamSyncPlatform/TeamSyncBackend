package com.teamsync.TeamSync.dtos.posts.comment;

import com.teamsync.TeamSync.dtos.posts.reaction.ReactionReference;
import com.teamsync.TeamSync.dtos.users.UserReference;
import com.teamsync.TeamSync.models.posts.Reaction;
import com.teamsync.TeamSync.models.users.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CommentDTO {
    private Long id;
    private String content;
    private UserReference author;
    private List<ReactionReference> reactions;
}
