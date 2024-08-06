package com.teamsync.TeamSync.models.groups;
import com.teamsync.TeamSync.models.posts.Post;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "channels")
@TableGenerator(name="channels_id_generator", table="primary_keys", pkColumnName="key_pk", pkColumnValue="channel", initialValue = 1, valueColumnName="value_pk")
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "channels_id_generator")
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    public void addPost(Post post) {
        posts.add(post);
    }
    public void removePost(Post post) {
        posts.remove(post);
    }

}