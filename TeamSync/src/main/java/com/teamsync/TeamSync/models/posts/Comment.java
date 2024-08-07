package com.teamsync.TeamSync.models.posts;

import com.teamsync.TeamSync.models.users.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "comments")
@TableGenerator(name="comments_id_generator", table="primary_keys", pkColumnName="key_pk", pkColumnValue="comment", initialValue = 1, valueColumnName="value_pk")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "comments_id_generator")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Reaction> reactions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public void addReaction(Reaction reaction) {
        reactions.add(reaction);
    }

    public void removeReaction(Reaction reaction) {
        reactions.remove(reaction);
    }
}