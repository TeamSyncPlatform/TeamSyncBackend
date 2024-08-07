package com.teamsync.TeamSync.services.posts.implementations;

import com.teamsync.TeamSync.models.posts.Attachment;
import com.teamsync.TeamSync.repositories.posts.IAttachmentRepository;
import com.teamsync.TeamSync.services.posts.interfaces.IAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@Service
public class AttachmentService implements IAttachmentService {
    @Autowired
    private IAttachmentRepository attachmentRepository;

    @Override
    public Collection<Attachment> getAll() {
        return attachmentRepository.findAll();
    }

    @Override
    public Attachment get(Long attachmentId) throws ResponseStatusException {
        return attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attachment not found"));
    }

    @Override
    public Attachment create(Attachment attachment) throws ResponseStatusException {
        return attachmentRepository.save(attachment);
    }

    @Override
    public Attachment update(Attachment attachment) throws ResponseStatusException {
        if (!attachmentRepository.existsById(attachment.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attachment not found");
        }
        return attachmentRepository.save(attachment);
    }

    @Override
    public Attachment remove(Long attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attachment not found"));
        attachmentRepository.delete(attachment);
        return attachment;
    }
}