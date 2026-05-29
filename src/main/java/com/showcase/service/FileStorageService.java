package com.showcase.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final String uploadDir;

    public FileStorageService(@Value("${app.upload.dir:uploads}") String uploadDir) {
        this.uploadDir = uploadDir;
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 파일 저장 후 접근 가능한 URL 경로 반환
     * 파일이 비어있으면 null 반환
     */
    public String store(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String original = file.getOriginalFilename();
        String ext = "";
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf("."));
        }

        String filename = UUID.randomUUID().toString() + ext;
        Path dest = Paths.get(uploadDir, filename);
        Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/" + filename;
    }

    /**
     * 로컬에 저장된 파일 삭제
     * /uploads/로 시작하는 경로만 삭제 (외부 URL은 무시)
     */
    public void delete(String urlPath) {
        if (urlPath == null || !urlPath.startsWith("/uploads/")) {
            return;
        }
        String filename = urlPath.substring("/uploads/".length());
        Path target = Paths.get(uploadDir, filename);
        try {
            Files.deleteIfExists(target);
        } catch (IOException e) {
            // 삭제 실패해도 DB 처리는 계속 진행
        }
    }
}
