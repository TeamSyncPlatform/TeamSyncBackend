package com.teamsync.TeamSync.models.groups;
import com.teamsync.TeamSync.models.posts.Post;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "channels")
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    private Boolean isDeleted = false;

    public void addPost(Post post) {
        posts.add(post);
    }
    public void removePost(Post post) {
        posts.remove(post);
    }

    public void delete(){
        isDeleted = true;
    }

}