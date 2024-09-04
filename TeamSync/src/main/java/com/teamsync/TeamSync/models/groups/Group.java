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

    @ManyToOne
    private User owner;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Group{id=").append(id)
                .append(", name='").append(name).append('\'')
                .append(", owner=").append(owner != null ? owner.getFirstName() + " " + owner.getLastName() : "null")
                .append(", isDeleted=").append(isDeleted)
                .append(", channels=").append(channels.size()).append(" channel(s)")
                .append(", members=").append(members.size()).append(" member(s)");

        sb.append(", membersDetails=[");
        members.forEach((userId, user) -> sb.append("User{id=").append(userId)
                .append(", name=").append(user.getFirstName())
                .append(" ").append(user.getLastName())
                .append("}, "));
        if (!members.isEmpty()) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("]}");

        return sb.toString();
    }
}