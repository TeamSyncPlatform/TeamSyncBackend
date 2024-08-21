package com.teamsync.TeamSync.dtos.analytics;

import com.teamsync.TeamSync.dtos.groups.group.GroupDTO;
import lombok.Data;

@Data
public class GroupPostsDTO {
    private GroupDTO group;
    private Long postsCount;

    public GroupPostsDTO(GroupDTO groupDTO, Long postsCount) {
        this.group = groupDTO;
        this.postsCount = postsCount;
    }
}
