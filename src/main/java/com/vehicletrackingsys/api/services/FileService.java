package com.vehicletrackingsys.api.services;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@Service
public class FileService {

    private final String UPLOAD_DIR;

    public FileService() throws IOException {
        this.UPLOAD_DIR = new ClassPathResource("static/img/").getFile().getAbsolutePath();
    }

    public boolean isFileTooLarge(MultipartFile file) {
        return file.getSize() > 5 * 1024 * 1024;
    }


    public String storeFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR, fileName);

        Files.createDirectories(filePath.getParent());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return "/img/" + fileName;
    }
}

