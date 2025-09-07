package com.peiwan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.peiwan.entity.CsEmployeeMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 客服员工关系响应DTO
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Data
@Schema(description = "客服员工关系响应")
public class CsEmployeeMappingResponse {

    @Schema(description = "关系ID")
    private Long id;

    @Schema(description = "客服用户ID")
    private Long csUserId;

    @Schema(description = "客服用户名")
    private String csUsername;

    @Schema(description = "客服真实姓名")
    private String csRealName;

    @Schema(description = "员工用户ID")
    private Long employeeUserId;

    @Schema(description = "员工用户名")
    private String employeeUsername;

    @Schema(description = "员工真实姓名")
    private String employeeRealName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    /**
     * 从CsEmployeeMapping实体转换为响应DTO
     */
    public static CsEmployeeMappingResponse fromEntity(CsEmployeeMapping mapping) {
        CsEmployeeMappingResponse response = new CsEmployeeMappingResponse();
        response.setId(mapping.getId());
        response.setCsUserId(mapping.getCsUserId());
        response.setEmployeeUserId(mapping.getEmployeeUserId());
        response.setCreatedAt(mapping.getCreatedAt());
        response.setUpdatedAt(mapping.getUpdatedAt());
        return response;
    }

    // Getter and Setter methods
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getCsUserId() { return csUserId; }
    public void setCsUserId(Long csUserId) { this.csUserId = csUserId; }
    
    public String getCsUsername() { return csUsername; }
    public void setCsUsername(String csUsername) { this.csUsername = csUsername; }
    
    public String getCsRealName() { return csRealName; }
    public void setCsRealName(String csRealName) { this.csRealName = csRealName; }
    
    public Long getEmployeeUserId() { return employeeUserId; }
    public void setEmployeeUserId(Long employeeUserId) { this.employeeUserId = employeeUserId; }
    
    public String getEmployeeUsername() { return employeeUsername; }
    public void setEmployeeUsername(String employeeUsername) { this.employeeUsername = employeeUsername; }
    
    public String getEmployeeRealName() { return employeeRealName; }
    public void setEmployeeRealName(String employeeRealName) { this.employeeRealName = employeeRealName; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
