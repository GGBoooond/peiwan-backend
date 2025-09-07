package com.peiwan.controller;

import com.peiwan.dto.ApiResponse;
import com.peiwan.service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
}

