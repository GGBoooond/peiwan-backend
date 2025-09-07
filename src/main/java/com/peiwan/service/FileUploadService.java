package com.peiwan.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传服务接口
 * 
 * @author peiwan
 * @since 2024-01-01
 */
public interface FileUploadService {

    /**
     * 上传图片文件
     */
    String uploadImage(MultipartFile file);

    /**
     * 删除文件
     */
    void deleteFile(String fileUrl);

    /**
     * 验证文件类型
     */
    boolean isValidImageType(MultipartFile file);

    /**
     * 验证文件大小
     */
    boolean isValidFileSize(MultipartFile file);
}

