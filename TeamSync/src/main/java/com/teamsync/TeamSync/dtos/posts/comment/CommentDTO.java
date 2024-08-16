package com.teamsync.TeamSync.dtos.posts.comment;

import com.teamsync.TeamSync.dtos.posts.post.PostReference;
import com.teamsync.TeamSync.dtos.users.UserReference;
import com.teamsync.TeamSync.models.posts.Post;
import com.teamsync.TeamSync.models.posts.Reaction;
import com.teamsync.TeamSync.models.posts.ReactionType;
import com.teamsync.TeamSync.models.users.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class CommentDTO {
    private Long id;
    private String content;
    private UserReference author;
    private Date creationDate;
    private Map<Long, ReactionType> reactions;
    private PostReference post;
}
