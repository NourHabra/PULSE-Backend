package com.pulse.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

public class ImageStorageUtil {

    public static String saveImage(MultipartFile file, String subfolder) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload empty file");
        }

        String baseDir = "uploads";
        String folderPath = baseDir + "/" + subfolder;

        Files.createDirectories(Paths.get(folderPath));

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(folderPath, fileName);

        Files.write(filePath, file.getBytes());

        // Return the public-facing URL/path
        return "/images/" + subfolder + "/" + fileName;
    }
}
