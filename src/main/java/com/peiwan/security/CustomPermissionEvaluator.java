package com.peiwan.security;

import com.peiwan.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 自定义权限评估器
 * 实现复杂的权限控制逻辑
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Slf4j
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String requestPath = (String) targetDomainObject;
        return evaluatePermission(authentication, requestPath);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return hasPermission(authentication, targetId, permission);
    }

    /**
     * 评估权限
     * 权限规则：
     * - 管理员(ADMIN)：可以访问任何接口
     * - 客服(CS)：可以访问客服(/cs/**)和员工(/employee**)接口
     * - 员工(EMPLOYEE)：只能访问员工(/employee**)接口
     */
    private boolean evaluatePermission(Authentication authentication, String requestPath) {
        try {
            // 获取用户角色
            User.UserRole userRole = getUserRole(authentication);
            if (userRole == null) {
                log.warn("无法获取用户角色");
                return false;
            }

            log.debug("权限评估: 用户角色={}, 请求路径={}", userRole, requestPath);

            // 管理员可以访问任何接口
            if (userRole == User.UserRole.ADMIN) {
                return true;
            }

            // 客服可以访问客服和员工接口
            if (userRole == User.UserRole.CS) {
                return requestPath.startsWith("/cs/") || requestPath.startsWith("/employee/");
            }

            // 员工只能访问员工接口
            if (userRole == User.UserRole.EMPLOYEE) {
                return requestPath.startsWith("/employee/");
            }

            return false;
        } catch (Exception e) {
            log.error("权限评估异常", e);
            return false;
        }
    }

    /**
     * 从认证信息中获取用户角色
     */
    private User.UserRole getUserRole(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof SessionUserDetailsService.SessionUserDetails) {
            return ((SessionUserDetailsService.SessionUserDetails) principal).getUserRole();
        }

        // 从权限中解析角色
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String role = authority.getAuthority();
            if (role.startsWith("ROLE_")) {
                String roleName = role.substring(5); // 移除 "ROLE_" 前缀
                try {
                    return User.UserRole.valueOf(roleName);
                } catch (IllegalArgumentException e) {
                    log.warn("未知的用户角色: {}", roleName);
                }
            }
        }

        return null;
    }
}
