package com.peiwan.security;

/**
 * 权限常量
 * 
 * @author peiwan
 * @since 2024-01-01
 */
public class PermissionConstants {
    
    // 管理员权限
    public static final String ADMIN_ACCESS = "hasRole('ADMIN')";
    
    // 客服权限（管理员和客服都可以访问）
    public static final String CS_ACCESS = "hasRole('ADMIN') or hasRole('CS')";
    
    // 员工权限（管理员、客服和员工都可以访问）
    public static final String EMPLOYEE_ACCESS = "hasRole('ADMIN') or hasRole('CS') or hasRole('EMPLOYEE')";
    
    // 自定义权限检查
    public static final String CUSTOM_PERMISSION = "@securityUtils.hasPermission(#path)";
}
