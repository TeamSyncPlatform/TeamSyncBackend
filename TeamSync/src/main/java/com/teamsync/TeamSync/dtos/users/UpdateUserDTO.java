package com.teamsync.TeamSync.dtos.users;

import com.teamsync.TeamSync.dtos.groups.group.GroupReference;
import com.teamsync.TeamSync.models.notifications.NotificationType;
import com.teamsync.TeamSync.models.users.Image;
import com.teamsync.TeamSync.models.users.Role;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
public class UpdateUserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String department;
    private String jobTitle;
    private List<String> skills;
    private Image profileImage;
}
