package com.teamsync.TeamSync.models.users;

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
@TableGenerator(name="user_id_generator", table="primary_keys", pkColumnName="key_pk", pkColumnValue="user", initialValue=1, valueColumnName="value_pk")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "user_id_generator")
    private Long id;

    @Column(unique = true)
    private String externalIdentification;

    @Column(unique = true)
    private String email;

    private String firstName;

    private String lastName;

    private String address;

    private String phoneNumber;

    private String department;

    private String jobTitle;

    @ElementCollection
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<String> skills;

    private Set<NotificationType> ignoredNotifications = new HashSet<NotificationType>();

}