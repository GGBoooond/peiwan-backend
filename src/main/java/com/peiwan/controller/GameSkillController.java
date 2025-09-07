package com.peiwan.controller;

import com.peiwan.dto.ApiResponse;
import com.peiwan.dto.GameSkillRequest;
import com.peiwan.dto.GameSkillResponse;
import com.peiwan.entity.GameSkill;
import com.peiwan.entity.EmployeeProfile;
import com.peiwan.mapper.GameSkillMapper;
import com.peiwan.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 游戏技能控制器
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/game-skills")
@Tag(name = "游戏技能管理", description = "游戏技能相关接口")
public class GameSkillController {

    private static final Logger log = LoggerFactory.getLogger(GameSkillController.class);

    private final GameSkillMapper gameSkillMapper;
    private final EmployeeService employeeService;

    public GameSkillController(GameSkillMapper gameSkillMapper, EmployeeService employeeService) {
        this.gameSkillMapper = gameSkillMapper;
        this.employeeService = employeeService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取游戏技能", description = "根据游戏技能ID获取详细信息")
    public ApiResponse<GameSkillResponse> getGameSkill(
            @Parameter(description = "游戏技能ID") @PathVariable Long id,
            HttpServletRequest httpRequest) {
        try {
            GameSkill gameSkill = gameSkillMapper.selectById(id);
            if (gameSkill == null) {
                return ApiResponse.<GameSkillResponse>error(404, "游戏技能不存在")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            GameSkillResponse response = GameSkillResponse.fromEntity(gameSkill);
            return ApiResponse.success("获取游戏技能成功", response)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("获取游戏技能失败: {}", e.getMessage());
            return ApiResponse.<GameSkillResponse>error(500, "获取游戏技能失败")
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @GetMapping("/profile/{profileId}")
    @Operation(summary = "获取员工的游戏技能列表", description = "根据员工资料ID获取其所有游戏技能")
    public ApiResponse<List<GameSkillResponse>> getGameSkillsByProfile(
            @Parameter(description = "员工资料ID") @PathVariable Long profileId,
            HttpServletRequest httpRequest) {
        try {
            List<GameSkill> gameSkills = gameSkillMapper.findByProfileId(profileId);
            List<GameSkillResponse> responses = gameSkills.stream()
                    .map(GameSkillResponse::fromEntity)
                    .collect(Collectors.toList());

            return ApiResponse.success("获取游戏技能列表成功", responses)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("获取游戏技能列表失败: {}", e.getMessage());
            return ApiResponse.<List<GameSkillResponse>>error(500, "获取游戏技能列表失败")
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @GetMapping("/my-skills")
    @Operation(summary = "获取当前员工的游戏技能", description = "员工获取自己的游戏技能列表")
    public ApiResponse<List<GameSkillResponse>> getMyGameSkills(HttpServletRequest httpRequest) {
        try {
            String userIdStr = httpRequest.getHeader("X-User-Id");
            if (userIdStr == null) {
                return ApiResponse.<List<GameSkillResponse>>error(401, "未找到用户信息")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            Long userId = Long.parseLong(userIdStr);
            EmployeeProfile profile = employeeService.findByUserId(userId);
            if (profile == null) {
                return ApiResponse.<List<GameSkillResponse>>error(404, "员工资料不存在")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            List<GameSkill> gameSkills = gameSkillMapper.findByProfileId(profile.getId());
            List<GameSkillResponse> responses = gameSkills.stream()
                    .map(GameSkillResponse::fromEntity)
                    .collect(Collectors.toList());

            return ApiResponse.success("获取我的游戏技能成功", responses)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("获取我的游戏技能失败: {}", e.getMessage());
            return ApiResponse.<List<GameSkillResponse>>error(500, "获取我的游戏技能失败")
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @GetMapping("/game/{gameName}")
    @Operation(summary = "根据游戏名称搜索技能", description = "根据游戏名称搜索相关的游戏技能")
    public ApiResponse<List<GameSkillResponse>> getGameSkillsByGame(
            @Parameter(description = "游戏名称") @PathVariable String gameName,
            HttpServletRequest httpRequest) {
        try {
            List<GameSkill> gameSkills = gameSkillMapper.findByGameName(gameName);
            List<GameSkillResponse> responses = gameSkills.stream()
                    .map(GameSkillResponse::fromEntity)
                    .collect(Collectors.toList());

            return ApiResponse.success("搜索游戏技能成功", responses)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("搜索游戏技能失败: {}", e.getMessage());
            return ApiResponse.<List<GameSkillResponse>>error(500, "搜索游戏技能失败")
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @PostMapping
    @Operation(summary = "创建游戏技能", description = "为当前员工创建新的游戏技能")
    public ApiResponse<GameSkillResponse> createGameSkill(
            @Valid @RequestBody GameSkillRequest request,
            HttpServletRequest httpRequest) {
        try {
            String userIdStr = httpRequest.getHeader("X-User-Id");
            if (userIdStr == null) {
                return ApiResponse.<GameSkillResponse>error(401, "未找到用户信息")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            Long userId = Long.parseLong(userIdStr);
            EmployeeProfile profile = employeeService.findByUserId(userId);
            if (profile == null) {
                profile = employeeService.createProfile(userId);
            }

            GameSkill gameSkill = new GameSkill();
            gameSkill.setProfileId(profile.getId());
            gameSkill.setGameName(request.getGameName());
            gameSkill.setPlayStyle(request.getPlayStyle());
            gameSkill.setServiceType(request.getServiceType());
            gameSkill.setHighestRank(request.getHighestRank());
            gameSkill.setCreatedAt(LocalDateTime.now());
            gameSkill.setUpdatedAt(LocalDateTime.now());
            gameSkill.setDeleted(0);

            gameSkillMapper.insert(gameSkill);

            GameSkillResponse response = GameSkillResponse.fromEntity(gameSkill);
            return ApiResponse.success("游戏技能创建成功", response)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("创建游戏技能失败: {}", e.getMessage());
            return ApiResponse.<GameSkillResponse>error(400, e.getMessage())
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新游戏技能", description = "更新指定的游戏技能信息")
    public ApiResponse<GameSkillResponse> updateGameSkill(
            @Parameter(description = "游戏技能ID") @PathVariable Long id,
            @Valid @RequestBody GameSkillRequest request,
            HttpServletRequest httpRequest) {
        try {
            String userIdStr = httpRequest.getHeader("X-User-Id");
            if (userIdStr == null) {
                return ApiResponse.<GameSkillResponse>error(401, "未找到用户信息")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            GameSkill gameSkill = gameSkillMapper.selectById(id);
            if (gameSkill == null) {
                return ApiResponse.<GameSkillResponse>error(404, "游戏技能不存在")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            // 验证所有权（确保只有技能所有者能修改）
            Long userId = Long.parseLong(userIdStr);
            EmployeeProfile profile = employeeService.findByUserId(userId);
            if (profile == null || !profile.getId().equals(gameSkill.getProfileId())) {
                return ApiResponse.<GameSkillResponse>error(403, "无权修改此游戏技能")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            gameSkill.setGameName(request.getGameName());
            gameSkill.setPlayStyle(request.getPlayStyle());
            gameSkill.setServiceType(request.getServiceType());
            gameSkill.setHighestRank(request.getHighestRank());
            gameSkill.setUpdatedAt(LocalDateTime.now());

            gameSkillMapper.updateById(gameSkill);

            GameSkillResponse response = GameSkillResponse.fromEntity(gameSkill);
            return ApiResponse.success("游戏技能更新成功", response)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("更新游戏技能失败: {}", e.getMessage());
            return ApiResponse.<GameSkillResponse>error(400, e.getMessage())
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除游戏技能", description = "删除指定的游戏技能")
    public ApiResponse<Void> deleteGameSkill(
            @Parameter(description = "游戏技能ID") @PathVariable Long id,
            HttpServletRequest httpRequest) {
        try {
            String userIdStr = httpRequest.getHeader("X-User-Id");
            if (userIdStr == null) {
                return ApiResponse.<Void>error(401, "未找到用户信息")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            GameSkill gameSkill = gameSkillMapper.selectById(id);
            if (gameSkill == null) {
                return ApiResponse.<Void>error(404, "游戏技能不存在")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            // 验证所有权（确保只有技能所有者能删除）
            Long userId = Long.parseLong(userIdStr);
            EmployeeProfile profile = employeeService.findByUserId(userId);
            if (profile == null || !profile.getId().equals(gameSkill.getProfileId())) {
                return ApiResponse.<Void>error(403, "无权删除此游戏技能")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            gameSkillMapper.deleteById(id);

            return ApiResponse.<Void>success("游戏技能删除成功", null)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("删除游戏技能失败: {}", e.getMessage());
            return ApiResponse.<Void>error(400, e.getMessage())
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @PostMapping("/batch")
    @Operation(summary = "批量创建游戏技能", description = "为当前员工批量创建游戏技能")
    public ApiResponse<List<GameSkillResponse>> createGameSkillsBatch(
            @Valid @RequestBody List<GameSkillRequest> requests,
            HttpServletRequest httpRequest) {
        try {
            String userIdStr = httpRequest.getHeader("X-User-Id");
            if (userIdStr == null) {
                return ApiResponse.<List<GameSkillResponse>>error(401, "未找到用户信息")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            Long userId = Long.parseLong(userIdStr);
            EmployeeProfile profile = employeeService.findByUserId(userId);
            if (profile == null) {
                profile = employeeService.createProfile(userId);
            }

            final EmployeeProfile finalProfile = profile;
            List<GameSkill> gameSkills = requests.stream().map(request -> {
                GameSkill gameSkill = new GameSkill();
                gameSkill.setProfileId(finalProfile.getId());
                gameSkill.setGameName(request.getGameName());
                gameSkill.setPlayStyle(request.getPlayStyle());
                gameSkill.setServiceType(request.getServiceType());
                gameSkill.setHighestRank(request.getHighestRank());
                gameSkill.setCreatedAt(LocalDateTime.now());
                gameSkill.setUpdatedAt(LocalDateTime.now());
                gameSkill.setDeleted(0);
                return gameSkill;
            }).collect(Collectors.toList());

            for (GameSkill gameSkill : gameSkills) {
                gameSkillMapper.insert(gameSkill);
            }

            List<GameSkillResponse> responses = gameSkills.stream()
                    .map(GameSkillResponse::fromEntity)
                    .collect(Collectors.toList());

            return ApiResponse.success("批量创建游戏技能成功", responses)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("批量创建游戏技能失败: {}", e.getMessage());
            return ApiResponse.<List<GameSkillResponse>>error(400, e.getMessage())
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }
}
