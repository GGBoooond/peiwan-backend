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
 * 游戏技能实体类
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "game_skills")
public class GameSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 员工资料ID
     */
    @NotNull(message = "员工资料ID不能为空")
    @Column(name = "profile_id", nullable = false)
    private Long profileId;

    /**
     * 游戏名称
     */
    @NotBlank(message = "游戏名称不能为空")
    @Size(max = 100, message = "游戏名称长度不能超过100个字符")
    @Column(name = "game_name", nullable = false, length = 100)
    private String gameName;

    /**
     * 陪玩类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "play_style", nullable = false, length = 20)
    private PlayStyle playStyle;

    /**
     * 服务类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", nullable = false, length = 20)
    private ServiceType serviceType;

    /**
     * 最高段位
     */
    @Size(max = 100, message = "最高段位长度不能超过100个字符")
    @Column(name = "highest_rank", length = 100)
    private String highestRank;

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
    
    public Long getProfileId() { return profileId; }
    public void setProfileId(Long profileId) { this.profileId = profileId; }
    
    public String getGameName() { return gameName; }
    public void setGameName(String gameName) { this.gameName = gameName; }
    
    public PlayStyle getPlayStyle() { return playStyle; }
    public void setPlayStyle(PlayStyle playStyle) { this.playStyle = playStyle; }
    
    public ServiceType getServiceType() { return serviceType; }
    public void setServiceType(ServiceType serviceType) { this.serviceType = serviceType; }
    
    public String getHighestRank() { return highestRank; }
    public void setHighestRank(String highestRank) { this.highestRank = highestRank; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }

    /**
     * 陪玩类型枚举
     */
    public enum PlayStyle {
        TECHNICAL("技术陪"),
        ENTERTAINMENT("娱乐陪");

        private final String description;

        PlayStyle(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 服务类型枚举
     */
    public enum ServiceType {
        RANKED("排位"),
        CASUAL("匹配");

        private final String description;

        ServiceType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}

