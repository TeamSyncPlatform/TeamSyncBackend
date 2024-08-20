package com.teamsync.TeamSync.services.users;

import com.teamsync.TeamSync.models.users.Image;
import com.teamsync.TeamSync.repositories.users.IImageRepository;
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
import java.util.UUID;

@Service
public class ImageService implements IImageService{

    @Autowired
    private IImageRepository imageRepository;
    private final String UPLOAD_DIR = "profile_pictures";

    @Override
    public Image uploadImage(MultipartFile file) throws IOException {
        String filePath = saveFileToFileSystem(file);

        Image image = new Image();
        image.setPath(filePath);
        image.setOriginalName(file.getOriginalFilename());

        return imageRepository.save(image);
    }

    @Override
    public Image getImage(Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() ->new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));
    }

    @Override
    public Resource getFileFromFileSystem(String filePath) throws IOException {
        Path path = Paths.get(filePath).normalize();
        Resource resource = new UrlResource(path.toUri());

        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new IOException("File not found or not readable");
        }
    }

    @Override
    public void removeImage(Long imageId) {
        Image image = getImage(imageId);
        removeFileFromFileSystem(image.getPath());
        imageRepository.delete(image);
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

    private void removeFileFromFileSystem(String filePath) {
        try {
            Path path = Paths.get(filePath);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file from file system", e);
        }
    }
}
