package com.peiwan.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 工单接单请求DTO
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Data
public class OrderAcceptRequest {

    /**
     * 接单截图URL
     */
    @NotBlank(message = "接单截图不能为空")
    private String imageUrl;

    // 手动添加getter/setter方法以确保编译通过
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}

