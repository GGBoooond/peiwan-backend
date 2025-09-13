package com.peiwan.security;

import com.peiwan.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 安全工具类
 * 提供便捷的权限检查方法
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Slf4j
@Component
public class SecurityUtils {

    /**
     * 获取当前用户ID
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SessionUserDetailsService.SessionUserDetails) {
            return ((SessionUserDetailsService.SessionUserDetails) authentication.getPrincipal()).getUserId();
        }
        return null;
    }

    /**
     * 获取当前用户角色
     */
    public static User.UserRole getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SessionUserDetailsService.SessionUserDetails) {
            return ((SessionUserDetailsService.SessionUserDetails) authentication.getPrincipal()).getUserRole();
        }
        return null;
    }

    /**
     * 获取当前用户信息
     */
    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SessionUserDetailsService.SessionUserDetails) {
            return ((SessionUserDetailsService.SessionUserDetails) authentication.getPrincipal()).getUser();
        }
        return null;
    }

    /**
     * 检查当前用户是否有访问指定路径的权限
     */
    public static boolean hasPermission(String path) {
        User.UserRole role = getCurrentUserRole();
        if (role == null) {
            return false;
        }

        switch (role) {
            case ADMIN:
                return true; // 管理员可以访问任何接口
            case CS:
                return path.startsWith("/cs/") || path.startsWith("/employee/"); // 客服可以访问客服和员工接口
            case EMPLOYEE:
                return path.startsWith("/employee/"); // 员工只能访问员工接口
            default:
                return false;
        }
    }

    /**
     * 检查当前用户是否为管理员
     */
    public static boolean isAdmin() {
        return User.UserRole.ADMIN.equals(getCurrentUserRole());
    }

    /**
     * 检查当前用户是否为客服
     */
    public static boolean isCS() {
        return User.UserRole.CS.equals(getCurrentUserRole());
    }

    /**
     * 检查当前用户是否为员工
     */
    public static boolean isEmployee() {
        return User.UserRole.EMPLOYEE.equals(getCurrentUserRole());
    }

    /**
     * 检查当前用户是否已认证
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}
