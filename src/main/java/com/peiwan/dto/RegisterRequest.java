package com.peiwan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 注册请求DTO
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Data
public class RegisterRequest {

    /**
     * 用户名（游戏昵称）
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 50, message = "用户名长度必须在2-50个字符之间")
    private String username;

    /**
     * 真实姓名
     */
    @NotBlank(message = "真实姓名不能为空")
    @Size(max = 50, message = "真实姓名长度不能超过50个字符")
    private String realName;

    /**
     * 手机号
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Size(max = 20, message = "手机号长度不能超过20个字符")
    private String phone;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 100, message = "密码长度必须在6-100个字符之间")
    private String password;

    /**
     * 确认密码
     */
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    // 手动添加getter/setter方法以确保编译通过
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}

