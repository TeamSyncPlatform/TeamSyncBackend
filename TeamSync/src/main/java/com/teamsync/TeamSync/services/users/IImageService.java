package com.teamsync.TeamSync.services.users;

import com.teamsync.TeamSync.models.users.Image;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IImageService {
    Image uploadImage(MultipartFile file) throws IOException;
    Image getImage(Long imageId);
    Resource getFileFromFileSystem(String filePath) throws IOException;
    void removeImage(Long imageId);
}
