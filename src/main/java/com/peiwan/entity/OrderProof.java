package com.peiwan.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 工单凭证实体类
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "order_proofs")
public class OrderProof {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 工单ID
     */
    @NotNull(message = "工单ID不能为空")
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    /**
     * 凭证类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "proof_type", nullable = false, length = 20)
    private ProofType proofType;

    /**
     * 图片URL
     */
    @NotBlank(message = "图片URL不能为空")
    @Size(max = 500, message = "图片URL长度不能超过500个字符")
    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    /**
     * 是否为重新提交
     */
    @Column(name = "is_resubmission")
    private Boolean isResubmission = false;

    /**
     * 是否为续单
     */
    @Column(name = "is_renewal")
    private Boolean isRenewal = false;

    /**
     * 上传时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 逻辑删除标记
     */
    @Column(name = "deleted")
    private Integer deleted = 0;

    // 手动添加getter/setter方法以确保编译通过
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    
    public ProofType getProofType() { return proofType; }
    public void setProofType(ProofType proofType) { this.proofType = proofType; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public Boolean getIsResubmission() { return isResubmission; }
    public void setIsResubmission(Boolean isResubmission) { this.isResubmission = isResubmission; }
    
    public Boolean getIsRenewal() { return isRenewal; }
    public void setIsRenewal(Boolean isRenewal) { this.isRenewal = isRenewal; }
    
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }

    /**
     * 凭证类型枚举
     */
    public enum ProofType {
        ACCEPTANCE("接单截图"),
        COMPLETION("完成截图");

        private final String description;

        ProofType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}

