package com.peiwan.dto;

import com.peiwan.entity.AuditLog;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 工单审核请求DTO
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Data
public class OrderAuditRequest {

    /**
     * 审核动作
     */
    @NotNull(message = "审核动作不能为空")
    private AuditLog.AuditAction action;

    /**
     * 审核意见
     */
    private String comments;

    // 手动添加getter/setter方法以确保编译通过
    public AuditLog.AuditAction getAction() { return action; }
    public void setAction(AuditLog.AuditAction action) { this.action = action; }
    
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
}

