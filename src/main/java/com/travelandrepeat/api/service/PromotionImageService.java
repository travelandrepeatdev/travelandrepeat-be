package com.travelandrepeat.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class PromotionImageService {
    @Value("${env.local}")
    private boolean isLocal;

    @Value("${env.imageResource}")
    private String imageResource;

    @Value("${volumes.upload-dir}")
    private String uploadDir;

    @Value("${volumes.upload-public-url}")
    private String uploadPublicUrl;

    public String save(MultipartFile file) {
        String filename;
        if (file != null && !file.isEmpty()) {
            String extension = extractExtension(file.getOriginalFilename());

            filename = UUID.randomUUID() + extension;
            log.info("Uploading file: {}", filename);
            try {
                String dir = uploadDir;
                if (isLocal){
                    // remove last slash and file:
                    dir = imageResource.substring(5, imageResource.length() - 1);
                }
                Path root = Paths.get(dir);
                Files.createDirectories(root);

                Path target = root.resolve(filename);

                file.transferTo(target);
            } catch (IOException e) {
                log.error("Failed to store file {} with name {}", file.getName(), filename, e);
            }

        } else {
            log.warn("Promotion image not contained");
            return null;
        }

        log.info("Promotion image saved as {}", uploadPublicUrl + filename);
        return uploadPublicUrl + filename;
    }

    private String extractExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(name -> name.contains("."))
                .map(name -> name.substring(name.lastIndexOf(".")))
                .orElse("");
    }

    public String remove(String imageUrl) {
        boolean wasDeleted = false;

        String dir = uploadDir;
        if (isLocal){
            // remove last slash
            dir = imageResource.substring(5, imageResource.length() - 1);
        }

        if (!imageUrl.isBlank()) {
            // remove public path for frontend
            imageUrl = imageUrl.split(uploadPublicUrl)[1];
            log.info("Promotion image {} to remove from dir local {}", imageUrl, uploadDir);
            Path root = Paths.get(dir);
            Path target = root.resolve(imageUrl);
            try {
                wasDeleted = Files.deleteIfExists(target);
            } catch (IOException e) {
                log.error("Failed to remove file {}", imageUrl, e);
            }
        }

        if (!wasDeleted) {
            log.warn("File does not exist {}", imageUrl);
            return null;
        }

        log.info("File {} has been deleted", imageUrl);
        return imageUrl;
    }
}
