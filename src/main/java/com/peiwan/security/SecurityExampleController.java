package com.peiwan.security;

import com.peiwan.dto.ApiResponse;
import com.peiwan.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 安全示例控制器
 * 演示如何使用新的权限控制
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/security-demo")
@Tag(name = "安全示例", description = "权限控制示例接口")
public class SecurityExampleController {

    @GetMapping("/admin-only")
    @Operation(summary = "仅管理员可访问", description = "只有管理员可以访问的接口")
    @PreAuthorize(PermissionConstants.ADMIN_ACCESS)
    public ApiResponse<String> adminOnly(HttpServletRequest request) {
        User currentUser = SecurityUtils.getCurrentUser();
        return ApiResponse.success("管理员接口访问成功", 
            "当前用户: " + currentUser.getUsername() + ", 角色: " + currentUser.getRole());
    }

    @GetMapping("/cs-access")
    @Operation(summary = "客服可访问", description = "管理员和客服可以访问的接口")
    @PreAuthorize(PermissionConstants.CS_ACCESS)
    public ApiResponse<String> csAccess(HttpServletRequest request) {
        User currentUser = SecurityUtils.getCurrentUser();
        return ApiResponse.success("客服接口访问成功", 
            "当前用户: " + currentUser.getUsername() + ", 角色: " + currentUser.getRole());
    }

    @GetMapping("/employee-access")
    @Operation(summary = "员工可访问", description = "管理员、客服和员工都可以访问的接口")
    @PreAuthorize(PermissionConstants.EMPLOYEE_ACCESS)
    public ApiResponse<String> employeeAccess(HttpServletRequest request) {
        User currentUser = SecurityUtils.getCurrentUser();
        return ApiResponse.success("员工接口访问成功", 
            "当前用户: " + currentUser.getUsername() + ", 角色: " + currentUser.getRole());
    }

    @GetMapping("/current-user")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    public ApiResponse<User> getCurrentUser() {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            return ApiResponse.error(401, "用户未登录");
        }
        return ApiResponse.success("获取当前用户信息成功", currentUser);
    }

    @GetMapping("/check-permission/{path}")
    @Operation(summary = "检查路径权限", description = "检查当前用户是否有访问指定路径的权限")
    public ApiResponse<Boolean> checkPermission(@PathVariable String path) {
        boolean hasPermission = SecurityUtils.hasPermission("/" + path);
        User.UserRole role = SecurityUtils.getCurrentUserRole();
        return ApiResponse.success("权限检查完成", hasPermission)
                .requestId("当前角色: " + (role != null ? role.name() : "未登录"));
    }
}
