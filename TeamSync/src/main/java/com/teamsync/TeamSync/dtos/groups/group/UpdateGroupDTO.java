package com.teamsync.TeamSync.dtos.groups.group;

import com.teamsync.TeamSync.models.groups.Channel;
import com.teamsync.TeamSync.models.users.User;
import lombok.Data;

import java.util.List;

@Data
public class UpdateGroupDTO {
    private Long id;
    private String name;
    private List<Channel> channels;
    private List<User> members;
}
