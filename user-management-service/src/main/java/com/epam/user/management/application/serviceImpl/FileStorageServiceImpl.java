package com.epam.user.management.application.serviceImpl;

import com.epam.user.management.application.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex + 1);
        }
        return "";
    }

    @Override
    public String storeFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot store empty file");
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String uniqueFilename = UUID.randomUUID().toString()+ "."+ fileExtension;
        if (!Files.exists(Path.of(uploadDir))) {
            Files.createDirectories(Path.of(uploadDir));
        }

        File destinationFile = new File(uploadDir, uniqueFilename);

        Files.copy(file.getInputStream(), Paths.get(destinationFile.getAbsolutePath()));

        String fileUri = (uploadDir + uniqueFilename);

        return fileUri;
    }
}
