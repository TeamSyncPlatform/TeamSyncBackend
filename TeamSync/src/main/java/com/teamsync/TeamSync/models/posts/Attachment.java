package com.teamsync.TeamSync.models.posts;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "attachments")
@TableGenerator(name="attachments_id_generator", table="primary_keys", pkColumnName="key_pk", pkColumnValue="attachment", initialValue = 1, valueColumnName="value_pk")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "attachments_id_generator")
    private Long id;

    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;


}
