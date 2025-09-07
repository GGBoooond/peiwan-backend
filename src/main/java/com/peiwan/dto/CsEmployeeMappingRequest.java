package com.peiwan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 客服员工关系请求DTO
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Data
@Schema(description = "客服员工关系请求")
public class CsEmployeeMappingRequest {

    @Schema(description = "关系ID，更新时需要")
    private Long id;

    @NotNull(message = "客服用户ID不能为空")
    @Schema(description = "客服用户ID", example = "1")
    private Long csUserId;

    @NotNull(message = "员工用户ID不能为空")
    @Schema(description = "员工用户ID", example = "2")
    private Long employeeUserId;

    /**
     * 批量创建请求
     */
    @Data
    @Schema(description = "批量创建客服员工关系请求")
    public static class BatchCreateRequest {

        @NotNull(message = "客服用户ID不能为空")
        @Schema(description = "客服用户ID", example = "1")
        private Long csUserId;

        @NotNull(message = "员工用户ID列表不能为空")
        @Schema(description = "员工用户ID列表", example = "[2, 3, 4]")
        private List<Long> employeeUserIds;
        
        // Getter and Setter methods
        public Long getCsUserId() { return csUserId; }
        public void setCsUserId(Long csUserId) { this.csUserId = csUserId; }
        
        public List<Long> getEmployeeUserIds() { return employeeUserIds; }
        public void setEmployeeUserIds(List<Long> employeeUserIds) { this.employeeUserIds = employeeUserIds; }
    }

    /**
     * 重新分配请求
     */
    @Data
    @Schema(description = "重新分配员工请求")
    public static class ReassignRequest {

        @NotNull(message = "员工用户ID不能为空")
        @Schema(description = "员工用户ID", example = "2")
        private Long employeeUserId;

        @NotNull(message = "新客服用户ID不能为空")
        @Schema(description = "新客服用户ID", example = "3")
        private Long newCsUserId;
        
        // Getter and Setter methods
        public Long getEmployeeUserId() { return employeeUserId; }
        public void setEmployeeUserId(Long employeeUserId) { this.employeeUserId = employeeUserId; }
        
        public Long getNewCsUserId() { return newCsUserId; }
        public void setNewCsUserId(Long newCsUserId) { this.newCsUserId = newCsUserId; }
    }

    // Main class Getter and Setter methods
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getCsUserId() { return csUserId; }
    public void setCsUserId(Long csUserId) { this.csUserId = csUserId; }
    
    public Long getEmployeeUserId() { return employeeUserId; }
    public void setEmployeeUserId(Long employeeUserId) { this.employeeUserId = employeeUserId; }
}
