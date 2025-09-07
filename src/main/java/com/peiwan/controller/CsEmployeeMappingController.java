package com.peiwan.controller;

import com.peiwan.dto.ApiResponse;
import com.peiwan.dto.CsEmployeeMappingRequest;
import com.peiwan.dto.CsEmployeeMappingResponse;
import com.peiwan.entity.CsEmployeeMapping;
import com.peiwan.entity.User;
import com.peiwan.mapper.UserMapper;
import com.peiwan.service.CsEmployeeMappingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 客服员工关系控制器
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/cs-employee-mappings")
@Tag(name = "客服员工关系管理", description = "客服员工关系相关接口")
public class CsEmployeeMappingController {

    private static final Logger log = LoggerFactory.getLogger(CsEmployeeMappingController.class);

    private final CsEmployeeMappingService csEmployeeMappingService;
    private final UserMapper userMapper;

    public CsEmployeeMappingController(CsEmployeeMappingService csEmployeeMappingService, UserMapper userMapper) {
        this.csEmployeeMappingService = csEmployeeMappingService;
        this.userMapper = userMapper;
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取客服员工关系", description = "根据关系ID获取详细信息")
    public ApiResponse<CsEmployeeMappingResponse> getMapping(
            @Parameter(description = "关系ID") @PathVariable Long id,
            HttpServletRequest httpRequest) {
        try {
            CsEmployeeMapping mapping = csEmployeeMappingService.findById(id);
            if (mapping == null) {
                return ApiResponse.<CsEmployeeMappingResponse>error(404, "客服员工关系不存在")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            CsEmployeeMappingResponse response = enrichMappingResponse(mapping);
            return ApiResponse.success("获取客服员工关系成功", response)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("获取客服员工关系失败: {}", e.getMessage());
            return ApiResponse.<CsEmployeeMappingResponse>error(500, "获取客服员工关系失败")
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @GetMapping("/cs/{csUserId}")
    @Operation(summary = "获取客服管理的员工关系", description = "根据客服ID获取其管理的所有员工关系")
    public ApiResponse<List<CsEmployeeMappingResponse>> getMappingsByCs(
            @Parameter(description = "客服用户ID") @PathVariable Long csUserId,
            HttpServletRequest httpRequest) {
        try {
            List<CsEmployeeMapping> mappings = csEmployeeMappingService.findByCsUserId(csUserId);
            List<CsEmployeeMappingResponse> responses = mappings.stream()
                    .map(this::enrichMappingResponse)
                    .collect(Collectors.toList());

            return ApiResponse.success("获取客服员工关系列表成功", responses)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("获取客服员工关系列表失败: {}", e.getMessage());
            return ApiResponse.<List<CsEmployeeMappingResponse>>error(500, "获取客服员工关系列表失败")
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @GetMapping("/employee/{employeeUserId}")
    @Operation(summary = "获取员工的客服关系", description = "根据员工ID获取其所属的客服关系")
    public ApiResponse<List<CsEmployeeMappingResponse>> getMappingsByEmployee(
            @Parameter(description = "员工用户ID") @PathVariable Long employeeUserId,
            HttpServletRequest httpRequest) {
        try {
            List<CsEmployeeMapping> mappings = csEmployeeMappingService.findByEmployeeUserId(employeeUserId);
            List<CsEmployeeMappingResponse> responses = mappings.stream()
                    .map(this::enrichMappingResponse)
                    .collect(Collectors.toList());

            return ApiResponse.success("获取员工客服关系列表成功", responses)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("获取员工客服关系列表失败: {}", e.getMessage());
            return ApiResponse.<List<CsEmployeeMappingResponse>>error(500, "获取员工客服关系列表失败")
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @GetMapping("/my-employees")
    @Operation(summary = "获取当前客服管理的员工", description = "客服获取自己管理的员工关系列表")
    public ApiResponse<List<CsEmployeeMappingResponse>> getMyEmployees(HttpServletRequest httpRequest) {
        try {
            String userIdStr = httpRequest.getHeader("X-User-Id");
            if (userIdStr == null) {
                return ApiResponse.<List<CsEmployeeMappingResponse>>error(401, "未找到用户信息")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            Long csUserId = Long.parseLong(userIdStr);
            List<CsEmployeeMapping> mappings = csEmployeeMappingService.findByCsUserId(csUserId);
            List<CsEmployeeMappingResponse> responses = mappings.stream()
                    .map(this::enrichMappingResponse)
                    .collect(Collectors.toList());

            return ApiResponse.success("获取我的员工关系成功", responses)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("获取我的员工关系失败: {}", e.getMessage());
            return ApiResponse.<List<CsEmployeeMappingResponse>>error(500, "获取我的员工关系失败")
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @GetMapping
    @Operation(summary = "获取所有客服员工关系", description = "管理员获取所有的客服员工关系（需要管理员权限）")
    public ApiResponse<List<CsEmployeeMappingResponse>> getAllMappings(HttpServletRequest httpRequest) {
        try {
            List<CsEmployeeMapping> mappings = csEmployeeMappingService.findAll();
            List<CsEmployeeMappingResponse> responses = mappings.stream()
                    .map(this::enrichMappingResponse)
                    .collect(Collectors.toList());

            return ApiResponse.success("获取所有客服员工关系成功", responses)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("获取所有客服员工关系失败: {}", e.getMessage());
            return ApiResponse.<List<CsEmployeeMappingResponse>>error(500, "获取所有客服员工关系失败")
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @PostMapping
    @Operation(summary = "创建客服员工关系", description = "创建新的客服员工关系")
    public ApiResponse<CsEmployeeMappingResponse> createMapping(
            @Valid @RequestBody CsEmployeeMappingRequest request,
            HttpServletRequest httpRequest) {
        try {
            CsEmployeeMapping mapping = csEmployeeMappingService.createMapping(
                    request.getCsUserId(), 
                    request.getEmployeeUserId()
            );

            CsEmployeeMappingResponse response = enrichMappingResponse(mapping);
            return ApiResponse.success("客服员工关系创建成功", response)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("创建客服员工关系失败: {}", e.getMessage());
            return ApiResponse.<CsEmployeeMappingResponse>error(400, e.getMessage())
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @PostMapping("/batch")
    @Operation(summary = "批量创建客服员工关系", description = "为一个客服批量分配多个员工")
    public ApiResponse<List<CsEmployeeMappingResponse>> createMappingsBatch(
            @Valid @RequestBody CsEmployeeMappingRequest.BatchCreateRequest request,
            HttpServletRequest httpRequest) {
        try {
            List<CsEmployeeMapping> mappings = csEmployeeMappingService.createMappings(
                    request.getCsUserId(),
                    request.getEmployeeUserIds()
            );

            List<CsEmployeeMappingResponse> responses = mappings.stream()
                    .map(this::enrichMappingResponse)
                    .collect(Collectors.toList());

            return ApiResponse.success("批量创建客服员工关系成功", responses)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("批量创建客服员工关系失败: {}", e.getMessage());
            return ApiResponse.<List<CsEmployeeMappingResponse>>error(400, e.getMessage())
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新客服员工关系", description = "更新指定的客服员工关系")
    public ApiResponse<CsEmployeeMappingResponse> updateMapping(
            @Parameter(description = "关系ID") @PathVariable Long id,
            @Valid @RequestBody CsEmployeeMappingRequest request,
            HttpServletRequest httpRequest) {
        try {
            CsEmployeeMapping mapping = csEmployeeMappingService.updateMapping(
                    id,
                    request.getCsUserId(),
                    request.getEmployeeUserId()
            );

            CsEmployeeMappingResponse response = enrichMappingResponse(mapping);
            return ApiResponse.success("客服员工关系更新成功", response)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("更新客服员工关系失败: {}", e.getMessage());
            return ApiResponse.<CsEmployeeMappingResponse>error(400, e.getMessage())
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @PostMapping("/reassign")
    @Operation(summary = "重新分配员工", description = "将员工重新分配给新的客服")
    public ApiResponse<CsEmployeeMappingResponse> reassignEmployee(
            @Valid @RequestBody CsEmployeeMappingRequest.ReassignRequest request,
            HttpServletRequest httpRequest) {
        try {
            csEmployeeMappingService.reassignEmployee(
                    request.getEmployeeUserId(),
                    request.getNewCsUserId()
            );

            // 获取新创建的关系
            CsEmployeeMapping newMapping = csEmployeeMappingService.findByCsAndEmployee(
                    request.getNewCsUserId(),
                    request.getEmployeeUserId()
            );

            CsEmployeeMappingResponse response = enrichMappingResponse(newMapping);
            return ApiResponse.success("员工重新分配成功", response)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("重新分配员工失败: {}", e.getMessage());
            return ApiResponse.<CsEmployeeMappingResponse>error(400, e.getMessage())
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除客服员工关系", description = "删除指定的客服员工关系")
    public ApiResponse<Void> deleteMapping(
            @Parameter(description = "关系ID") @PathVariable Long id,
            HttpServletRequest httpRequest) {
        try {
            csEmployeeMappingService.deleteMapping(id);

            return ApiResponse.<Void>success("客服员工关系删除成功", null)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("删除客服员工关系失败: {}", e.getMessage());
            return ApiResponse.<Void>error(400, e.getMessage())
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @DeleteMapping("/cs/{csUserId}")
    @Operation(summary = "删除客服的所有员工关系", description = "删除指定客服的所有员工关系")
    public ApiResponse<Void> deleteMappingsByCsUserId(
            @Parameter(description = "客服用户ID") @PathVariable Long csUserId,
            HttpServletRequest httpRequest) {
        try {
            csEmployeeMappingService.deleteByCsUserId(csUserId);

            return ApiResponse.<Void>success("删除客服的所有员工关系成功", null)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("删除客服的所有员工关系失败: {}", e.getMessage());
            return ApiResponse.<Void>error(400, e.getMessage())
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @DeleteMapping("/employee/{employeeUserId}")
    @Operation(summary = "删除员工的所有关系", description = "删除指定员工的所有客服关系")
    public ApiResponse<Void> deleteMappingsByEmployeeUserId(
            @Parameter(description = "员工用户ID") @PathVariable Long employeeUserId,
            HttpServletRequest httpRequest) {
        try {
            csEmployeeMappingService.deleteByEmployeeUserId(employeeUserId);

            return ApiResponse.<Void>success("删除员工的所有关系成功", null)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("删除员工的所有关系失败: {}", e.getMessage());
            return ApiResponse.<Void>error(400, e.getMessage())
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @GetMapping("/check")
    @Operation(summary = "检查客服员工关系", description = "检查指定客服是否管理指定员工")
    public ApiResponse<Boolean> checkMapping(
            @Parameter(description = "客服用户ID") @RequestParam Long csUserId,
            @Parameter(description = "员工用户ID") @RequestParam Long employeeUserId,
            HttpServletRequest httpRequest) {
        try {
            boolean isManaging = csEmployeeMappingService.isCsManageEmployee(csUserId, employeeUserId);

            return ApiResponse.success("检查客服员工关系成功", isManaging)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("检查客服员工关系失败: {}", e.getMessage());
            return ApiResponse.<Boolean>error(500, "检查客服员工关系失败")
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    /**
     * 丰富关系响应信息，添加用户详细信息
     */
    private CsEmployeeMappingResponse enrichMappingResponse(CsEmployeeMapping mapping) {
        CsEmployeeMappingResponse response = CsEmployeeMappingResponse.fromEntity(mapping);

        // 获取用户信息
        User csUser = userMapper.selectById(mapping.getCsUserId());
        User employeeUser = userMapper.selectById(mapping.getEmployeeUserId());

        if (csUser != null) {
            response.setCsUsername(csUser.getUsername());
            response.setCsRealName(csUser.getRealName());
        }

        if (employeeUser != null) {
            response.setEmployeeUsername(employeeUser.getUsername());
            response.setEmployeeRealName(employeeUser.getRealName());
        }

        return response;
    }
}
