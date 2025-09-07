package com.peiwan.controller;

import com.peiwan.dto.ApiResponse;
import com.peiwan.service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件上传控制器
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/upload")
@Tag(name = "文件上传", description = "文件上传相关接口")
public class FileUploadController {

    private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);

    private final FileUploadService fileUploadService;

    @Value("${file.upload.path}")
    private String uploadPath;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("/image")
    @Operation(summary = "上传图片", description = "上传图片文件")
    public ApiResponse<String> uploadImage(@RequestParam("file") MultipartFile file, HttpServletRequest httpRequest) {
        try {
            if (file.isEmpty()) {
                return ApiResponse.<String>error(400, "文件不能为空")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            String fileUrl = fileUploadService.uploadImage(file);
            return ApiResponse.success("图片上传成功", fileUrl)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("图片上传失败: {}", e.getMessage());
            return ApiResponse.<String>error(500, e.getMessage())
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @GetMapping("/view/{year}/{month}/{day}/{filename:.+}")
    @Operation(summary = "查看上传的图片", description = "通过路径访问已上传的图片文件")
    public ResponseEntity<Resource> viewImage(
            @PathVariable String year,
            @PathVariable String month, 
            @PathVariable String day,
            @PathVariable String filename) {
        try {
            // 构建文件路径
            Path filePath = Paths.get(uploadPath, year, month, day, filename);
            Resource resource = new FileSystemResource(filePath);
            
            if (!resource.exists() || !resource.isReadable()) {
                log.warn("文件不存在或不可读: {}", filePath);
                return ResponseEntity.notFound().build();
            }
            
            // 检查文件是否在上传目录范围内（安全检查）
            Path uploadDir = Paths.get(uploadPath).toAbsolutePath().normalize();
            Path requestedFile = filePath.toAbsolutePath().normalize();
            if (!requestedFile.startsWith(uploadDir)) {
                log.warn("尝试访问上传目录外的文件: {}", requestedFile);
                return ResponseEntity.badRequest().build();
            }
            
            // 根据文件扩展名设置Content-Type
            String contentType = determineContentType(filename);
            
            log.info("访问图片文件: {}", filePath);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CACHE_CONTROL, "max-age=3600") // 缓存1小时
                    .body(resource);
                    
        } catch (Exception e) {
            log.error("访问图片文件失败: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * 根据文件名确定Content-Type
     */
    private String determineContentType(String filename) {
        if (filename == null) {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        
        String extension = filename.toLowerCase();
        if (extension.endsWith(".png")) {
            return MediaType.IMAGE_PNG_VALUE;
        } else if (extension.endsWith(".jpg") || extension.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG_VALUE;
        } else if (extension.endsWith(".gif")) {
            return MediaType.IMAGE_GIF_VALUE;
        } else {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
    }
}

