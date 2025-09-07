package com.peiwan.service.impl;

import com.peiwan.service.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.UUID;

/**
 * 文件上传服务实现类
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {

    private static final Logger log = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.upload.allowed-types}")
    private String allowedTypes;

    @Value("${file.upload.max-size}")
    private long maxFileSize;

    private static final String[] ALLOWED_IMAGE_TYPES = {"image/jpeg", "image/jpg", "image/png", "image/gif"};

    @Override
    public String uploadImage(MultipartFile file) {
        try {
            // 验证文件
            if (!isValidImageType(file)) {
                throw new RuntimeException("不支持的文件类型");
            }

            if (!isValidFileSize(file)) {
                throw new RuntimeException("文件大小超出限制");
            }

            // 创建上传目录
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            Path uploadDir = Paths.get(uploadPath, datePath);
            Files.createDirectories(uploadDir);

            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = UUID.randomUUID().toString() + extension;

            // 保存文件
            Path filePath = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), filePath);

            // 返回文件URL
            String fileUrl = "/uploads/" + datePath + "/" + filename;
            log.info("文件上传成功: {}", fileUrl);
            return fileUrl;

        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败");
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        try {
            if (fileUrl != null && fileUrl.startsWith("/uploads/")) {
                String relativePath = fileUrl.substring(8); // 移除 "/uploads/"
                Path filePath = Paths.get(uploadPath, relativePath);
                Files.deleteIfExists(filePath);
                log.info("文件删除成功: {}", fileUrl);
            }
        } catch (IOException e) {
            log.error("文件删除失败: {}", fileUrl, e);
        }
    }

    @Override
    public boolean isValidImageType(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        String contentType = file.getContentType();
        return Arrays.asList(ALLOWED_IMAGE_TYPES).contains(contentType);
    }

    @Override
    public boolean isValidFileSize(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        return file.getSize() <= maxFileSize;
    }
}

