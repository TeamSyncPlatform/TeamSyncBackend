package com.teamsync.TeamSync.dtos.posts.post;

import com.teamsync.TeamSync.models.posts.Attachment;
import com.teamsync.TeamSync.models.posts.Comment;
import com.teamsync.TeamSync.models.posts.Reaction;
import com.teamsync.TeamSync.models.users.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class PostDTO {
    private Long id;
    private String content;
    private Date creationDate;
    private List<Comment> comments;
    private List<Attachment> attachments;
    private List<Reaction> reactions;
    private User author;
}
