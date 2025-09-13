package com.peiwan.controller;

import com.peiwan.dto.ApiResponse;
import com.peiwan.dto.ChangePasswordRequest;
import com.peiwan.dto.LoginRequest;
import com.peiwan.dto.LoginResponse;
import com.peiwan.dto.RegisterRequest;
import com.peiwan.entity.User;
import com.peiwan.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "认证管理", description = "用户登录、注册等认证相关接口")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户使用用户名和密码登录系统")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest, HttpSession session) {
        try {
            LoginResponse response = userService.login(request);
            
            // 将用户信息存储到session中
            User user = userService.findByUsername(request.getUsername());
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("userRole", user.getRole().name());
            
            return ApiResponse.<LoginResponse>success("登录成功", response)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("登录失败: {}", e.getMessage());
            return ApiResponse.<LoginResponse>error(401, e.getMessage())
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "新用户注册账号")
    public ApiResponse<User> register(@Valid @RequestBody RegisterRequest request, HttpServletRequest httpRequest) {
        try {
            User user = userService.register(request);
            return ApiResponse.<User>success("注册成功", user)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("注册失败: {}", e.getMessage());
            return ApiResponse.<User>error(400, e.getMessage())
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户登出系统")
    public ApiResponse<Void> logout(HttpServletRequest httpRequest, HttpSession session) {
        // 清除session
        session.invalidate();
        return ApiResponse.<Void>success("登出成功", null)
                .requestId(httpRequest.getHeader("X-Request-Id"));
    }

    @GetMapping("/me")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    public ApiResponse<User> getCurrentUser(HttpServletRequest httpRequest, HttpSession session) {
        try {
            // 从session中获取用户ID
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                return ApiResponse.<User>error(401, "未找到用户信息")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            User user = userService.findById(userId);
            if (user == null) {
                return ApiResponse.<User>error(404, "用户不存在")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            return ApiResponse.<User>success("获取用户信息成功", user)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("获取用户信息失败: {}", e.getMessage());
            return ApiResponse.<User>error(500, "获取用户信息失败")
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @GetMapping("/check-username/{username}")
    @Operation(summary = "检查用户名可用性", description = "检查用户名是否已被使用")
    public ApiResponse<Boolean> checkUsernameAvailability(@PathVariable String username, HttpServletRequest httpRequest) {
        try {
            User existingUser = userService.findByUsername(username);
            boolean available = existingUser == null;
            return ApiResponse.<Boolean>success("检查完成", available)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("检查用户名失败: {}", e.getMessage());
            return ApiResponse.<Boolean>error(500, "检查用户名失败")
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @GetMapping("/check-phone/{phone}")
    @Operation(summary = "检查手机号可用性", description = "检查手机号是否已被使用")
    public ApiResponse<Boolean> checkPhoneAvailability(@PathVariable String phone, HttpServletRequest httpRequest) {
        try {
            User existingUser = userService.findByPhone(phone);
            boolean available = existingUser == null;
            return ApiResponse.<Boolean>success("检查完成", available)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("检查手机号失败: {}", e.getMessage());
            return ApiResponse.<Boolean>error(500, "检查手机号失败")
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @PostMapping("/change-password")
    @Operation(summary = "修改用户密码", description = "用户修改自己的登录密码")
    public ApiResponse<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request, 
                                          HttpServletRequest httpRequest, 
                                          HttpSession session) {
        try {
            // 从session中获取用户ID
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                return ApiResponse.<Void>error(401, "用户未登录")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            // 调用服务层修改密码
            userService.changePassword(userId, request);
            
            return ApiResponse.<Void>success("密码修改成功", null)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("修改密码失败: {}", e.getMessage());
            return ApiResponse.<Void>error(400, e.getMessage())
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }
}
