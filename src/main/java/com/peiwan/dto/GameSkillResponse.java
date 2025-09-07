package com.peiwan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.peiwan.entity.GameSkill;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 游戏技能响应DTO
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Data
@Schema(description = "游戏技能响应")
public class GameSkillResponse {

    @Schema(description = "游戏技能ID")
    private Long id;

    @Schema(description = "员工资料ID")
    private Long profileId;

    @Schema(description = "游戏名称")
    private String gameName;

    @Schema(description = "陪玩类型")
    private GameSkill.PlayStyle playStyle;

    @Schema(description = "陪玩类型描述")
    private String playStyleDescription;

    @Schema(description = "服务类型")
    private GameSkill.ServiceType serviceType;

    @Schema(description = "服务类型描述")
    private String serviceTypeDescription;

    @Schema(description = "最高段位")
    private String highestRank;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    /**
     * 从GameSkill实体转换为响应DTO
     */
    public static GameSkillResponse fromEntity(GameSkill gameSkill) {
        GameSkillResponse response = new GameSkillResponse();
        response.setId(gameSkill.getId());
        response.setProfileId(gameSkill.getProfileId());
        response.setGameName(gameSkill.getGameName());
        response.setPlayStyle(gameSkill.getPlayStyle());
        response.setPlayStyleDescription(gameSkill.getPlayStyle() != null ? gameSkill.getPlayStyle().getDescription() : null);
        response.setServiceType(gameSkill.getServiceType());
        response.setServiceTypeDescription(gameSkill.getServiceType() != null ? gameSkill.getServiceType().getDescription() : null);
        response.setHighestRank(gameSkill.getHighestRank());
        response.setCreatedAt(gameSkill.getCreatedAt());
        response.setUpdatedAt(gameSkill.getUpdatedAt());
        return response;
    }

    // Getter and Setter methods
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getProfileId() { return profileId; }
    public void setProfileId(Long profileId) { this.profileId = profileId; }
    
    public String getGameName() { return gameName; }
    public void setGameName(String gameName) { this.gameName = gameName; }
    
    public GameSkill.PlayStyle getPlayStyle() { return playStyle; }
    public void setPlayStyle(GameSkill.PlayStyle playStyle) { this.playStyle = playStyle; }
    
    public String getPlayStyleDescription() { return playStyleDescription; }
    public void setPlayStyleDescription(String playStyleDescription) { this.playStyleDescription = playStyleDescription; }
    
    public GameSkill.ServiceType getServiceType() { return serviceType; }
    public void setServiceType(GameSkill.ServiceType serviceType) { this.serviceType = serviceType; }
    
    public String getServiceTypeDescription() { return serviceTypeDescription; }
    public void setServiceTypeDescription(String serviceTypeDescription) { this.serviceTypeDescription = serviceTypeDescription; }
    
    public String getHighestRank() { return highestRank; }
    public void setHighestRank(String highestRank) { this.highestRank = highestRank; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
