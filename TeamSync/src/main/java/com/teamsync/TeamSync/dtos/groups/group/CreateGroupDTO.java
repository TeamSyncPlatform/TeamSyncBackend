package com.teamsync.TeamSync.dtos.groups.group;

import com.teamsync.TeamSync.dtos.groups.channel.ChannelReference;
import com.teamsync.TeamSync.dtos.users.UserReference;
import com.teamsync.TeamSync.models.groups.Channel;
import com.teamsync.TeamSync.models.users.User;
import lombok.Data;

import java.util.List;

@Data
public class CreateGroupDTO {
    private String name;
    private List<ChannelReference> channels;
    private List<UserReference> members;
}
