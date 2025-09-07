package com.peiwan.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 工单实体类
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Order {

    private Long id;

    /**
     * 工单编号
     */
    private String orderNumber;

    /**
     * 委托人信息
     */
    private String clientInfo;

    /**
     * 工单状态
     */
    private OrderStatus status;

    /**
     * 分配的员工ID
     */
    private Long assignedEmployeeId;

    /**
     * 创建工单的客服ID
     */
    private Long createdByCsId;

    /**
     * 派单信息截图URL
     */
    private String orderInfoScreenshotUrl;

    /**
     * 接单时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime acceptedAt;

    /**
     * 完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completedAt;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    /**
     * 逻辑删除标记
     */
    private Integer deleted = 0;

    // 手动添加getter/setter方法以确保编译通过
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    
    public String getClientInfo() { return clientInfo; }
    public void setClientInfo(String clientInfo) { this.clientInfo = clientInfo; }
    
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    
    public Long getAssignedEmployeeId() { return assignedEmployeeId; }
    public void setAssignedEmployeeId(Long assignedEmployeeId) { this.assignedEmployeeId = assignedEmployeeId; }
    
    public Long getCreatedByCsId() { return createdByCsId; }
    public void setCreatedByCsId(Long createdByCsId) { this.createdByCsId = createdByCsId; }
    
    public String getOrderInfoScreenshotUrl() { return orderInfoScreenshotUrl; }
    public void setOrderInfoScreenshotUrl(String orderInfoScreenshotUrl) { this.orderInfoScreenshotUrl = orderInfoScreenshotUrl; }
    
    public LocalDateTime getAcceptedAt() { return acceptedAt; }
    public void setAcceptedAt(LocalDateTime acceptedAt) { this.acceptedAt = acceptedAt; }
    
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }

    /**
     * 工单状态枚举
     */
    public enum OrderStatus {
        PENDING_ACCEPTANCE("待接单"),
        IN_PROGRESS("进行中"),
        PENDING_AUDIT("待审核"),
        COMPLETED("已结单"),
        REJECTED("未通过");

        private final String description;

        OrderStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
