package com.peiwan.controller;

import cn.hutool.crypto.digest.BCrypt;
import com.peiwan.dto.ApiResponse;
import com.peiwan.entity.Order;
import com.peiwan.entity.User;
import com.peiwan.service.OrderService;
import com.peiwan.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 管理员控制器
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/admin")
@Tag(name = "管理员管理", description = "管理员相关接口")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private final UserService userService;
    private final OrderService orderService;

    public AdminController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/users")
    @Operation(summary = "获取用户列表", description = "管理员获取所有用户列表")
    public ApiResponse<List<User>> getUsers(HttpServletRequest httpRequest) {
        try {
            List<User> users = userService.findAllActive();
            return ApiResponse.success("获取用户列表成功", users)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("获取用户列表失败: {}", e.getMessage());
            return ApiResponse.<List<User>>error(500, "获取用户列表失败")
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @GetMapping("/orders")
    @Operation(summary = "获取所有工单", description = "管理员获取所有工单列表")
    public ApiResponse<List<Order>> getAllOrders(HttpServletRequest httpRequest) {
        try {
            List<Order> orders = orderService.findByStatus(null);
            return ApiResponse.success("获取工单列表成功", orders)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("获取工单列表失败: {}", e.getMessage());
            return ApiResponse.<List<Order>>error(500, "获取工单列表失败")
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @PostMapping("/users")
    @Operation(summary = "创建客服用户", description = "管理员创建新客服用户，默认密码为123456")
    public ApiResponse<User> createUser(@RequestBody User user, HttpServletRequest httpRequest) {
        try {
            //管理员创建的用户默认密码为123456
            user.setPasswordHash(BCrypt.hashpw("123456", BCrypt.gensalt()));
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            user.setRole(User.UserRole.CS);
            userService.createUser(user);
            return ApiResponse.success("用户创建成功", new User())
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("创建用户失败: {}", e.getMessage());
            return ApiResponse.<User>error(400, e.getMessage())
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @PutMapping("/users/{userId}")
    @Operation(summary = "更新用户", description = "管理员更新用户信息")
    public ApiResponse<User> updateUser(@PathVariable Long userId, @RequestBody User user, HttpServletRequest httpRequest) {
        try {
            user.setId(userId);
            User updatedUser = userService.updateUser(user);
            return ApiResponse.success("用户更新成功", updatedUser)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("更新用户失败: {}", e.getMessage());
            return ApiResponse.<User>error(400, e.getMessage())
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @DeleteMapping("/users/{userId}")
    @Operation(summary = "删除用户", description = "管理员删除用户")
    public ApiResponse<Void> deleteUser(@PathVariable Long userId, HttpServletRequest httpRequest) {
        try {
            userService.deleteUser(userId);
            return ApiResponse.<Void>success("用户删除成功", null)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("删除用户失败: {}", e.getMessage());
            return ApiResponse.<Void>error(400, e.getMessage())
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }
}

