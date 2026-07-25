package com.cdac.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cdac.exception.FileStorageException;
import com.cdac.service.FileStorageService;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public String storeFile(MultipartFile file) {
    	
    	String contentType = file.getContentType();

    	if (contentType == null ||
    	        !contentType.startsWith("image/")) {

    	    throw new FileStorageException(
    	            "Only image files are allowed.");
    	}

        if (file == null || file.isEmpty()) {
            throw new FileStorageException("File cannot be empty.");
        }

        try {

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName =
                    UUID.randomUUID() + "_" + file.getOriginalFilename();

            Path filePath = uploadPath.resolve(fileName);

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (IOException e) {
            throw new FileStorageException("Failed to store file.");
        }
    }

    @Override
    public void deleteFile(String fileUrl) {

        if (fileUrl == null || fileUrl.isBlank()) {
            return;
        }

        try {

            Path filePath = Paths.get(uploadDir)
                    .resolve(fileUrl);

            Files.deleteIfExists(filePath);

        } catch (IOException e) {
            throw new FileStorageException("Failed to delete file.");
        }
    }
}