package com.teamsync.TeamSync.dtos.notifications;

import com.teamsync.TeamSync.dtos.groups.channel.ChannelDTO;
import com.teamsync.TeamSync.dtos.groups.group.GroupDTO;
import com.teamsync.TeamSync.dtos.users.UserDTO;
import com.teamsync.TeamSync.models.groups.Channel;
import com.teamsync.TeamSync.models.groups.Group;
import com.teamsync.TeamSync.models.users.User;
import lombok.Data;

@Data
public class NewPostNotificationDTO {
    ChannelDTO channel;
    GroupDTO group;
    UserDTO author;
    UserDTO user;

    public NewPostNotificationDTO(GroupDTO group, ChannelDTO channel, UserDTO author, UserDTO user) {
        this.group = group;
        this.channel = channel;
        this.author = author;
        this.user = user;
    }
}
