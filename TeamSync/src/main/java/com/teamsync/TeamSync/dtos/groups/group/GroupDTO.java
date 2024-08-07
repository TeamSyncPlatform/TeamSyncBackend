package com.teamsync.TeamSync.dtos.groups.group;

import com.teamsync.TeamSync.dtos.groups.channel.ChannelReference;
import com.teamsync.TeamSync.dtos.users.UserReference;
import com.teamsync.TeamSync.models.groups.Channel;
import com.teamsync.TeamSync.models.users.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GroupDTO {
    private Long id;
    private String name;
    private List<ChannelReference> channels;
    private List<UserReference> members;
}
