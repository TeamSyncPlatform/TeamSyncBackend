package com.teamsync.TeamSync.repositories.posts;

import com.teamsync.TeamSync.models.posts.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAttachmentRepository extends JpaRepository<Attachment, Long> {

}