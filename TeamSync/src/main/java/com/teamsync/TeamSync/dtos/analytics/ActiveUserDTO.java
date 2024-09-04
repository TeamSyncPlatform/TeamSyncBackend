package com.teamsync.TeamSync.dtos.analytics;

import com.teamsync.TeamSync.dtos.users.UserDTO;
import com.teamsync.TeamSync.models.users.User;
import lombok.Data;

@Data
public class ActiveUserDTO {
    private UserDTO user;
    private Long activityCount;

    public ActiveUserDTO(UserDTO key, Long value) {
        this.user = key;
        this.activityCount = value;
    }
}
