package com.pulse.user.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

@RestController
public class ImageController {

    private final Path rootLocation = Paths.get("./uploads/profile_pictures");

    @GetMapping("/uploads/profile_pictures/{imageName}")
    public ResponseEntity<Resource> getProfilePicture(@PathVariable String imageName) {
        try {
            Path file = rootLocation.resolve(imageName);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()) {
                // Get the file's content type based on its extension
                String contentType = Files.probeContentType(file);
                if (contentType == null) {
                    contentType = "application/octet-stream"; // Default to binary if content type can't be determined
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/uploads/diagnosis_attachment/{fileName}")
    public ResponseEntity<Resource> getDiagnosisAttachment(@PathVariable String fileName) {
        try {
            Path file = Paths.get("./uploads/diagnosis_attachment").resolve(fileName);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()) {
                // Set the content type to PDF
                String contentType = "application/pdf"; // PDF files
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    @GetMapping("/uploads/id_images/{imageName}")
    public ResponseEntity<Resource> getIdImage(@PathVariable String imageName) {
        try {
            Path file = Paths.get("./uploads/id_images").resolve(imageName);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()) {
                // Get the file's content type based on its extension (image)
                String contentType = Files.probeContentType(file);
                if (contentType == null) {
                    contentType = "application/octet-stream"; // Default to binary if content type can't be determined
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    @GetMapping("/uploads/lab_results/{fileName}")
    public ResponseEntity<Resource> getLabResult(@PathVariable String fileName) {
        try {
            Path file = Paths.get("./uploads/lab_results").resolve(fileName);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()) {
                // Set the content type to PDF
                String contentType = "application/pdf"; // PDF files
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

}
