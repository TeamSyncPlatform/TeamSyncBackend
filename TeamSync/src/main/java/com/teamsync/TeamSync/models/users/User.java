package com.teamsync.TeamSync.models.users;

import com.teamsync.TeamSync.models.groups.Group;
import com.teamsync.TeamSync.models.notifications.NotificationType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "users")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID externalIdentification;

    @Column(unique = true)
    private String email;

    private String firstName;

    private String lastName;

    private String address;

    private String phoneNumber;

    @Enumerated
    private Role role = Role.USER;

    @Transient
    private String jwt;

    private String department;

    private String jobTitle;

    @ElementCollection
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<String> skills;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Group> groups;

    private Boolean isDeleted = false;

    private Set<NotificationType> ignoredNotifications = new HashSet<NotificationType>();

    public void addGroup(Group group){
        groups.add(group);
    }

    public void removeGroup(Group group){
        groups.remove(group);
    }

}