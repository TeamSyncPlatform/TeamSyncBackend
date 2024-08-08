package com.teamsync.TeamSync.dtos.users;

import com.teamsync.TeamSync.dtos.groups.group.GroupReference;
import com.teamsync.TeamSync.models.notifications.NotificationType;
import com.teamsync.TeamSync.models.users.Role;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
public class UserDTO {
    private Long id;
    private UUID externalIdentification;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private Role role;
    private String jwt;
    private String department;
    private String jobTitle;
    private List<String> skills;
    private GroupReference group;
    private Set<NotificationType> ignoredNotifications;
}
