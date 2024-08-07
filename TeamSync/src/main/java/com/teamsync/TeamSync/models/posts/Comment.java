package com.teamsync.TeamSync.models.posts;

import com.teamsync.TeamSync.models.users.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

//    @ElementCollection
//    @Cascade(org.hibernate.annotations.CascadeType.ALL)
//    private List<Reaction> reactions = new ArrayList<>();

    @ElementCollection
    @MapKeyColumn(name = "user_id")
    @Column(name = "reaction_type")
    @CollectionTable(name = "comment_reactions", joinColumns = @JoinColumn(name = "comment_id"))
    private Map<Long, ReactionType> reactions = new HashMap<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public void addReaction(Reaction reaction) {
        reactions.put(reaction.getUserId(), reaction.getType());
    }

    public void removeReaction(Reaction reaction) {
        reactions.remove(reaction.getUserId());
    }
}