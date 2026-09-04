package com.vicentedev.api_re.service.impl;

import com.vicentedev.api_re.exception.FileStorageException;
import com.vicentedev.api_re.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class LocalFileStorageServiceImpl implements FileStorageService {

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "webp");
    private final Path rootLocation;

    public LocalFileStorageServiceImpl(@Value("${file.upload-dir}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.rootLocation);
        } catch (IOException ex) {
            throw new FileStorageException("Could not initialize upload directory", ex);
        }
    }

    @Override
    public String storeFile(MultipartFile file, String subDirectory) {
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("Cannot store an empty file");
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "");
        String extension = getFileExtension(originalFilename);

        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new FileStorageException("Invalid file type: ." + extension + ". Allowed: " + ALLOWED_EXTENSIONS);
        }

        String newFilename = UUID.randomUUID() + "." + extension.toLowerCase();

        try {
            Path targetDirectory = this.rootLocation.resolve(subDirectory).normalize();
            Files.createDirectories(targetDirectory);

            Path targetLocation = targetDirectory.resolve(newFilename).normalize();
            if (!targetLocation.startsWith(this.rootLocation)) {
                throw new FileStorageException("Cannot store file outside current directory");
            }

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + subDirectory + "/" + newFilename;
        } catch (IOException ex) {
            throw new FileStorageException("Failed to store file " + originalFilename, ex);
        }
    }

    @Override
    public Resource loadFileAsResource(String filePath) {
        try {
            String cleanRelative = filePath.startsWith("/uploads/") ? filePath.substring("/uploads/".length()) : filePath;
            Path file = this.rootLocation.resolve(cleanRelative).normalize();
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new FileStorageException("File not found: " + filePath);
            }
        } catch (MalformedURLException ex) {
            throw new FileStorageException("File not found: " + filePath, ex);
        }
    }

    @Override
    public void deleteFile(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            return;
        }

        try {
            String cleanRelative = filePath.startsWith("/uploads/") ? filePath.substring("/uploads/".length()) : filePath;
            Path file = this.rootLocation.resolve(cleanRelative).normalize();
            if (file.startsWith(this.rootLocation)) {
                Files.deleteIfExists(file);
            }
        } catch (IOException ex) {
            throw new FileStorageException("Could not delete file: " + filePath, ex);
        }
    }

    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return filename.substring(dotIndex + 1);
        }
        return "";
    }
}
