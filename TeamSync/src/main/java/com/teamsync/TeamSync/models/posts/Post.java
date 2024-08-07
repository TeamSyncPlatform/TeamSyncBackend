package com.teamsync.TeamSync.models.posts;

import com.teamsync.TeamSync.models.users.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;

import java.util.*;

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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Attachment> attachments = new ArrayList<>();

//    @ElementCollection
//    @Cascade(org.hibernate.annotations.CascadeType.ALL)
//    private List<Reaction> reactions = new ArrayList<>();

    @ElementCollection
    @MapKeyColumn(name = "user_id")
    @Column(name = "reaction_type")
    @CollectionTable(name = "post_reactions", joinColumns = @JoinColumn(name = "post_id"))
    private Map<Long, ReactionType> reactions = new HashMap<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
    }

    public void addReaction(Reaction reaction) {
        reactions.put(reaction.getUserId(), reaction.getType());
    }

    public void removeReaction(Reaction reaction) {
        reactions.remove(reaction.getUserId());
    }

    public void addAttachment(Attachment attachment) {
        attachments.add(attachment);
    }

    public void removeAttachment(Attachment attachment) {
        attachments.remove(attachment);
    }
}
