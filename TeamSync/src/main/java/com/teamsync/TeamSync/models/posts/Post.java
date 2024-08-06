package com.teamsync.TeamSync.models.posts;

import com.teamsync.TeamSync.models.users.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "posts")
@TableGenerator(name="posts_id_generator", table="primary_keys", pkColumnName="key_pk", pkColumnValue="post", initialValue = 1, valueColumnName="value_pk")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "posts_id_generator")
    private Long id;

    private String content;

    private Date creationDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reaction> reactions = new ArrayList<>();

    @ManyToOne
    private User author;

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
    }

    public void addReaction(Reaction reaction) {
        reactions.add(reaction);
    }

    public void removeReaction(Reaction reaction) {
        reactions.remove(reaction);
    }

    public void addAttachment(Attachment attachment) {
        attachments.add(attachment);
    }

    public void removeAttachment(Attachment attachment) {
        attachments.remove(attachment);
    }
}
