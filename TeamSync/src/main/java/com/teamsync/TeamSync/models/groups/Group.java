package com.teamsync.TeamSync.models.groups;

import com.teamsync.TeamSync.models.posts.Post;
import com.teamsync.TeamSync.models.users.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "groups")
@TableGenerator(name="groups_id_generator", table="primary_keys", pkColumnName="key_pk", pkColumnValue="group", initialValue = 1, valueColumnName="value_pk")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "groups_id_generator")
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)

    private List<Channel> channels = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<User> members = new ArrayList<>();

    public void addChannel(Channel channel) {
        channels.add(channel);
    }
    public void removeChannel(Channel channel) {
        channels.remove(channel);
    }

    public void addMember(User user) {
        members.add(user);
    }
    public void removeMember(User user) {
        members.remove(user);
    }
}