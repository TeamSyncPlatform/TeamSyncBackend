package com.teamsync.TeamSync.services.posts.interfaces;

import com.teamsync.TeamSync.models.posts.Attachment;
import com.teamsync.TeamSync.models.posts.Post;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Collection;

public interface IAttachmentService {
    Collection<Attachment> getAll();
    Attachment get(Long attachmentId) throws ResponseStatusException;
    Attachment create(Attachment attachment) throws ResponseStatusException;
    Attachment update(Attachment attachment) throws ResponseStatusException;
    Attachment remove(Long attachmentId);
    Attachment uploadAttachment(MultipartFile file, Post post) throws IOException;
    Resource getFileFromFileSystem(String fileName) throws IOException;
}
