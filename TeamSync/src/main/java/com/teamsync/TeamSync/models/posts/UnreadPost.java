package com.teamsync.TeamSync.models.posts;

import com.teamsync.TeamSync.models.groups.Channel;
import com.teamsync.TeamSync.models.users.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "unread_posts")
public class UnreadPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @Column(name = "last_read_timestamp")
    private LocalDateTime lastReadTimestamp;

    public UnreadPost(User user, Channel channel, LocalDateTime lastReadTimestamp) {
        this.user = user;
        this.channel = channel;
        this.lastReadTimestamp = lastReadTimestamp;
    }

    public UnreadPost() {

    }
}