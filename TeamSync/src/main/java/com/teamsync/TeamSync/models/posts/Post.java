package com.teamsync.TeamSync.models.posts;

import com.teamsync.TeamSync.models.groups.Channel;
import com.teamsync.TeamSync.models.users.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;

import java.util.*;

@Entity
@Data
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Date creationDate;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments = new ArrayList<>();

    @ElementCollection
    @MapKeyColumn(name = "user_id")
    @Column(name = "reaction_type")
    @CollectionTable(name = "post_reactions", joinColumns = @JoinColumn(name = "post_id"))
    private Map<Long, ReactionType> reactions = new HashMap<>();

    @ManyToOne()
    private User author;

    private Boolean isDeleted = false;

    @ManyToOne()
    @JoinColumn(name = "post_id", nullable = false)
    private Channel channel;

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

    public void delete(){
        isDeleted = true;
    }

    @Override
    public String toString() {
        return "Post{id=" + id + ", content='" + content + "'}";
    }
}
