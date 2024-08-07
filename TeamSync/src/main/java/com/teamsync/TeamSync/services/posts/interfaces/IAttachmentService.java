package com.teamsync.TeamSync.services.posts.interfaces;

import com.teamsync.TeamSync.models.posts.Attachment;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

public interface IAttachmentService {
    Collection<Attachment> getAll();
    Attachment get(Long attachmentId) throws ResponseStatusException;
    Attachment create(Attachment attachment) throws ResponseStatusException;
    Attachment update(Attachment attachment) throws ResponseStatusException;
    Attachment remove(Long attachmentId);
}
