package com.example.htaproject2308.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    public static File saveAndGetFile(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new IllegalArgumentException("파일 이름이 비어 있습니다.");
        }

        // 파일을 저장할 경로 생성
        Path directoryPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath); // 디렉토리 생성
        }

        // 전체 파일 경로 설정
        Path filePath = directoryPath.resolve(originalFileName);

        // 파일 저장
        file.transferTo(filePath.toFile());

        return filePath.toFile();
    }
}

