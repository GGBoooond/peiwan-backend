package com.peiwan.dto;

import com.peiwan.entity.GameSkill;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 游戏技能请求DTO
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Data
@Schema(description = "游戏技能请求")
public class GameSkillRequest {

    @Schema(description = "游戏技能ID，更新时需要")
    private Long id;

    @NotBlank(message = "游戏名称不能为空")
    @Size(max = 100, message = "游戏名称长度不能超过100个字符")
    @Schema(description = "游戏名称", example = "王者荣耀")
    private String gameName;

    @NotNull(message = "陪玩类型不能为空")
    @Schema(description = "陪玩类型", example = "TECHNICAL")
    private GameSkill.PlayStyle playStyle;

    @NotNull(message = "服务类型不能为空")
    @Schema(description = "服务类型", example = "RANKED")
    private GameSkill.ServiceType serviceType;

    @Size(max = 100, message = "最高段位长度不能超过100个字符")
    @Schema(description = "最高段位", example = "王者50星")
    private String highestRank;

    // Getter and Setter methods
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getGameName() { return gameName; }
    public void setGameName(String gameName) { this.gameName = gameName; }
    
    public GameSkill.PlayStyle getPlayStyle() { return playStyle; }
    public void setPlayStyle(GameSkill.PlayStyle playStyle) { this.playStyle = playStyle; }
    
    public GameSkill.ServiceType getServiceType() { return serviceType; }
    public void setServiceType(GameSkill.ServiceType serviceType) { this.serviceType = serviceType; }
    
    public String getHighestRank() { return highestRank; }
    public void setHighestRank(String highestRank) { this.highestRank = highestRank; }
}
