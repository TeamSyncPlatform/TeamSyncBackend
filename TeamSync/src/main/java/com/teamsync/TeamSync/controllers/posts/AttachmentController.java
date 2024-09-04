package com.teamsync.TeamSync.controllers.posts;

import com.teamsync.TeamSync.dtos.posts.attachment.AttachmentDTO;
import com.teamsync.TeamSync.dtos.posts.attachment.CreateAttachmentDTO;
import com.teamsync.TeamSync.dtos.posts.attachment.GetAttachmentDTO;
import com.teamsync.TeamSync.dtos.posts.attachment.UpdateAttachmentDTO;
import com.teamsync.TeamSync.models.posts.Attachment;
import com.teamsync.TeamSync.models.posts.Post;
import com.teamsync.TeamSync.services.posts.interfaces.IAttachmentService;
import com.teamsync.TeamSync.services.posts.interfaces.IPostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.stream.Collectors;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/attachments")
public class AttachmentController {
    private final IAttachmentService service;
    private final ModelMapper mapper;
    private final IPostService postService;

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

    @PostMapping("/upload")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AttachmentDTO> uploadAttachment(
            @RequestParam("file") MultipartFile file,
            @RequestParam("postId") Long postId) {

        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            Post post = postService.get(postId);
            Attachment savedAttachment = service.uploadAttachment(file, post);
            return new ResponseEntity<>(mapper.map(savedAttachment, AttachmentDTO.class), HttpStatus.CREATED);

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/download")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Resource> getFile(@RequestBody GetAttachmentDTO attachmentDTO) {
        try {
            Path path = Paths.get(attachmentDTO.getPath()).normalize();
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}