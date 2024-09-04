package com.teamsync.TeamSync.dtos.groups.group;

import com.teamsync.TeamSync.dtos.groups.channel.ChannelReference;
import com.teamsync.TeamSync.dtos.users.UserReference;
import com.teamsync.TeamSync.models.groups.Channel;
import com.teamsync.TeamSync.models.users.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class GroupDTO {
    private Long id;
    private String name;
    private UserReference owner;
    private List<ChannelReference> channels;
    private Map<Long, UserReference> members;
}
