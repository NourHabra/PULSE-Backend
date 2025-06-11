package com.pulse.util;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

public class FileStorageUtil {

    public static String saveFile(MultipartFile file, String subfolder) throws IOException {
        if (file.isEmpty()) throw new IllegalArgumentException("Cannot upload empty file");

        // Directory where files will be stored
        String baseDir = "uploads";
        String folderPath = baseDir + "/" + subfolder;
        Files.createDirectories(Paths.get(folderPath));

        String fileExtension = getFileExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + fileExtension;

        Path filePath = Paths.get(folderPath, fileName);
        Files.write(filePath, file.getBytes());

        return "https://localhost:8443/uploads/" + subfolder + "/" + fileName;
    }

    private static String getFileExtension(String filename) {
        if (filename == null) return "";
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex);
    }
}
