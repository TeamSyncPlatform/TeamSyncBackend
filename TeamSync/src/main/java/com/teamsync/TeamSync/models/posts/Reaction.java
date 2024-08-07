package com.teamsync.TeamSync.models.posts;

import com.teamsync.TeamSync.models.users.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "reactions")
@TableGenerator(name="reactions_id_generator", table="primary_keys", pkColumnName="key_pk", pkColumnValue="reaction", initialValue = 1, valueColumnName="value_pk")

public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "reactions_id_generator")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated
    private ReactionType type;
}
