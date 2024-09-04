package com.teamsync.TeamSync.services.posts.implementations;

import com.teamsync.TeamSync.models.posts.Attachment;
import com.teamsync.TeamSync.models.posts.Comment;
import com.teamsync.TeamSync.models.posts.Post;
import com.teamsync.TeamSync.repositories.posts.IAttachmentRepository;
import com.teamsync.TeamSync.repositories.posts.IPostRepository;
import com.teamsync.TeamSync.services.posts.interfaces.IAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.UUID;

@Service
public class AttachmentService implements IAttachmentService {
    @Autowired
    private IAttachmentRepository attachmentRepository;

    @Autowired
    private IPostRepository postRepository;

    private final String UPLOAD_DIR = "uploads";

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
        Post post = postRepository.findById(attachment.getPost().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        attachment.setPost(post);

        Attachment savedAttachment = attachmentRepository.save(attachment);

        // Update the post's attachments list
        post.addAttachment(savedAttachment);
        postRepository.save(post);

        return savedAttachment;
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

        removeFileFromFileSystem(attachment.getPath());

        attachmentRepository.delete(attachment);
        return attachment;
    }

    @Override
    public Attachment uploadAttachment(MultipartFile file, Post post) throws IOException {
        String filePath = saveFileToFileSystem(file);

        Attachment attachment = new Attachment();
        attachment.setPost(post);
        attachment.setPath(filePath);
        attachment.setOriginalName(file.getOriginalFilename());

        return create(attachment);
    }

    private String saveFileToFileSystem(MultipartFile file) throws IOException {
        Path uploadDir = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        String fileName = UUID.randomUUID().toString();
        Path filePath = uploadDir.resolve(fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();
    }

    public Resource getFileFromFileSystem(String fileName) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new IOException("File not found or not readable");
        }
    }

    public void removeFileFromFileSystem(String filePath) {
        try {
            Path path = Paths.get(filePath);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete file from file system", e);
        }
    }
}