package com.teamsync.TeamSync.models.groups;

import com.teamsync.TeamSync.models.posts.Comment;
import com.teamsync.TeamSync.models.posts.Post;
import com.teamsync.TeamSync.models.users.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Data
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Channel> channels = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "group_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Map<Long, User> members = new HashMap<>();

    private Boolean isDeleted = false;

    public void addChannel(Channel channel) {
        channels.add(channel);
    }
    public void removeChannel(Channel channel) {
        channels.remove(channel);
    }

    public void addMember(User user) {
        members.put(user.getId(), user);
    }

    public void removeMember(User user) {
        members.remove(user.getId());
    }
    public void delete(){
        isDeleted = true;
    }
}