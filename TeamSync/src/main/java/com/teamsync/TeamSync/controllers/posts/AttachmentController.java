package com.teamsync.TeamSync.controllers.posts;

import com.teamsync.TeamSync.dtos.posts.attachment.AttachmentDTO;
import com.teamsync.TeamSync.dtos.posts.attachment.CreateAttachmentDTO;
import com.teamsync.TeamSync.dtos.posts.attachment.UpdateAttachmentDTO;
import com.teamsync.TeamSync.models.posts.Attachment;
import com.teamsync.TeamSync.services.posts.interfaces.IAttachmentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/attachments")
public class AttachmentController {
    private final IAttachmentService service;
    private final ModelMapper mapper;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Collection<AttachmentDTO>> getAttachments() {
        Collection<Attachment> attachments = service.getAll();
        Collection<AttachmentDTO> attachmentResponses = attachments.stream()
                .map(attachment -> mapper.map(attachment, AttachmentDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(attachmentResponses, HttpStatus.OK);
    }

    @GetMapping("/{attachmentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AttachmentDTO> get(@PathVariable Long attachmentId) {
        Attachment attachment = service.get(attachmentId);
        if (attachment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapper.map(attachment, AttachmentDTO.class), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AttachmentDTO> create(@RequestBody CreateAttachmentDTO attachment) {
        return new ResponseEntity<>(mapper.map(service.create(mapper.map(attachment, Attachment.class)), AttachmentDTO.class), HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public AttachmentDTO update(@RequestBody UpdateAttachmentDTO attachment) {
        return mapper.map(service.update(mapper.map(attachment, Attachment.class)), AttachmentDTO.class);
    }

    @DeleteMapping("/{attachmentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AttachmentDTO> remove(@PathVariable Long attachmentId) {
        Attachment attachment = service.remove(attachmentId);
        if (attachment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapper.map(attachment, AttachmentDTO.class), HttpStatus.OK);
    }
}