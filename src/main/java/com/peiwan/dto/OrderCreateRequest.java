package com.peiwan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 工单创建请求DTO
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Data
public class OrderCreateRequest {

    /**
     * 分配的员工ID
     */
    @NotNull(message = "员工ID不能为空")
    private Long assignedEmployeeId;

    /**
     * 委托人信息
     */
    @NotBlank(message = "委托人信息不能为空")
    private String clientInfo;

    /**
     * 派单信息截图URL
     */
    private String orderInfoScreenshotUrl;

    // 手动添加getter/setter方法以确保编译通过
    public Long getAssignedEmployeeId() { return assignedEmployeeId; }
    public void setAssignedEmployeeId(Long assignedEmployeeId) { this.assignedEmployeeId = assignedEmployeeId; }
    
    public String getClientInfo() { return clientInfo; }
    public void setClientInfo(String clientInfo) { this.clientInfo = clientInfo; }
    
    public String getOrderInfoScreenshotUrl() { return orderInfoScreenshotUrl; }
    public void setOrderInfoScreenshotUrl(String orderInfoScreenshotUrl) { this.orderInfoScreenshotUrl = orderInfoScreenshotUrl; }
}

