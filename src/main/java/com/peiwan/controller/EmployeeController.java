package com.peiwan.controller;

import com.peiwan.dto.ApiResponse;
import com.peiwan.dto.OrderCompleteRequest;
import com.peiwan.dto.OrderAcceptRequest;
import com.peiwan.entity.EmployeeProfile;
import com.peiwan.entity.GameSkill;
import com.peiwan.entity.Order;
import com.peiwan.service.EmployeeService;
import com.peiwan.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工控制器
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/employee")
@Tag(name = "员工管理", description = "员工相关接口")
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;
    private final OrderService orderService;

    public EmployeeController(EmployeeService employeeService, OrderService orderService) {
        this.employeeService = employeeService;
        this.orderService = orderService;
    }

    @GetMapping("/profile")
    @Operation(summary = "获取个人资料", description = "员工获取个人资料")
    public ApiResponse<EmployeeProfile> getProfile(HttpServletRequest httpRequest) {
        try {
            // 从请求头获取员工ID
            String employeeIdStr = httpRequest.getHeader("X-User-Id");
            if (employeeIdStr == null) {
                return ApiResponse.<EmployeeProfile>error(401, "未找到用户信息")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            Long employeeId = Long.parseLong(employeeIdStr);
            EmployeeProfile profile = employeeService.findByUserId(employeeId);
            if (profile == null) {
                profile = employeeService.createProfile(employeeId);
            }
            return ApiResponse.success("获取个人资料成功", profile)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("获取个人资料失败: {}", e.getMessage());
            return ApiResponse.<EmployeeProfile>error(500, "获取个人资料失败")
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    //更新员工状态
    @PutMapping("/profile")
    @Operation(summary = "更新员工状态", description = "员工更新个人资料")
    public ApiResponse<EmployeeProfile> updateProfile(@RequestBody EmployeeProfile profile, HttpServletRequest httpRequest) {
        try {
            // 从请求头获取员工ID
            String employeeIdStr = httpRequest.getHeader("X-User-Id");
            if (employeeIdStr == null) {
                return ApiResponse.<EmployeeProfile>error(401, "未找到用户信息")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            Long employeeId = Long.parseLong(employeeIdStr);
            profile.setUserId(employeeId);
            EmployeeProfile updatedProfile = employeeService.updateWorkStatus(employeeId, profile.getWorkStatus());
            return ApiResponse.success("个人资料更新成功", updatedProfile)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("更新个人资料失败: {}", e.getMessage());
            return ApiResponse.<EmployeeProfile>error(400, e.getMessage())
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @GetMapping("/orders")
    @Operation(summary = "获取分配的工单", description = "员工获取分配的工单列表")
    public ApiResponse<List<Order>> getOrders(HttpServletRequest httpRequest) {
        try {
            // 从请求头获取员工ID
            String employeeIdStr = httpRequest.getHeader("X-User-Id");
            if (employeeIdStr == null) {
                return ApiResponse.<List<Order>>error(401, "未找到用户信息")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            Long employeeId = Long.parseLong(employeeIdStr);
            List<Order> orders = orderService.findByEmployeeId(employeeId);
            return ApiResponse.success("获取工单列表成功", orders)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("获取工单列表失败: {}", e.getMessage());
            return ApiResponse.<List<Order>>error(500, "获取工单列表失败")
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @PostMapping("/orders/{orderId}/accept")
    @Operation(summary = "接单", description = "员工接单")
    public ApiResponse<Order> acceptOrder(@PathVariable Long orderId, @RequestBody OrderAcceptRequest request, HttpServletRequest httpRequest) {
        try {
            // 从请求头获取员工ID
            String employeeIdStr = httpRequest.getHeader("X-User-Id");
            if (employeeIdStr == null) {
                return ApiResponse.<Order>error(401, "未找到用户信息")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            Long employeeId = Long.parseLong(employeeIdStr);
            Order order = orderService.acceptOrder(orderId, request.getImageUrl(), employeeId);
            return ApiResponse.success("接单成功", order)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("接单失败: {}", e.getMessage());
            return ApiResponse.<Order>error(400, e.getMessage())
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @PostMapping("/orders/{orderId}/complete")
    @Operation(summary = "完成订单", description = "员工完成订单")
    public ApiResponse<Order> completeOrder(@PathVariable Long orderId, @RequestBody OrderCompleteRequest request, HttpServletRequest httpRequest) {
        try {
            // 从请求头获取员工ID
            String employeeIdStr = httpRequest.getHeader("X-User-Id");
            if (employeeIdStr == null) {
                return ApiResponse.<Order>error(401, "未找到用户信息")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            Long employeeId = Long.parseLong(employeeIdStr);
            Order order = orderService.completeOrder(orderId, request, employeeId);
            return ApiResponse.success("订单完成成功", order)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("完成订单失败: {}", e.getMessage());
            return ApiResponse.<Order>error(400, e.getMessage())
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @PostMapping("/orders/{orderId}/renew")
    @Operation(summary = "续单", description = "员工发起续单")
    public ApiResponse<Order> renewOrder(@PathVariable Long orderId, HttpServletRequest httpRequest) {
        try {
            // 从请求头获取员工ID
            String employeeIdStr = httpRequest.getHeader("X-User-Id");
            if (employeeIdStr == null) {
                return ApiResponse.<Order>error(401, "未找到用户信息")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            Long employeeId = Long.parseLong(employeeIdStr);
            Order order = orderService.renewOrder(orderId, employeeId);
            return ApiResponse.success("续单创建成功", order)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("续单失败: {}", e.getMessage());
            return ApiResponse.<Order>error(400, e.getMessage())
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @PostMapping("/orders/{orderId}/reSubmit")
    @Operation(summary = "重新上交", description = "工单审核失败后员工重新提交")
    public ApiResponse<Order> reSubmit(@PathVariable Long orderId,  @RequestBody OrderCompleteRequest request, HttpServletRequest httpRequest) {
        try {
            // 从请求头获取员工ID
            String employeeIdStr = httpRequest.getHeader("X-User-Id");
            if (employeeIdStr == null) {
                return ApiResponse.<Order>error(401, "未找到用户信息")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            Long employeeId = Long.parseLong(employeeIdStr);
            Order order = orderService.reSubmitOrder(orderId, request, employeeId);
            return ApiResponse.success("续单创建成功", order)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("续单失败: {}", e.getMessage());
            return ApiResponse.<Order>error(400, e.getMessage())
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @GetMapping("/game-skills")
    @Operation(summary = "获取我的游戏技能", description = "员工获取自己的游戏技能列表")
    public ApiResponse<List<GameSkill>> getMyGameSkills(HttpServletRequest httpRequest) {
        try {
            String employeeIdStr = httpRequest.getHeader("X-User-Id");
            if (employeeIdStr == null) {
                return ApiResponse.<List<GameSkill>>error(401, "未找到用户信息")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            Long employeeId = Long.parseLong(employeeIdStr);
            EmployeeProfile profile = employeeService.findByUserId(employeeId);
            if (profile == null) {
                profile = employeeService.createProfile(employeeId);
            }

            List<GameSkill> gameSkills = employeeService.findGameSkillsByProfileId(profile.getId());
            return ApiResponse.success("获取我的游戏技能成功", gameSkills)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("获取我的游戏技能失败: {}", e.getMessage());
            return ApiResponse.<List<GameSkill>>error(500, "获取我的游戏技能失败")
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @PutMapping("/game-skills")
    @Operation(summary = "更新我的游戏技能", description = "员工更新自己的游戏技能列表")
    public ApiResponse<List<GameSkill>> updateMyGameSkills(@RequestBody List<GameSkill> gameSkills, HttpServletRequest httpRequest) {
        try {
            String employeeIdStr = httpRequest.getHeader("X-User-Id");
            if (employeeIdStr == null) {
                return ApiResponse.<List<GameSkill>>error(401, "未找到用户信息")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            Long employeeId = Long.parseLong(employeeIdStr);
            List<GameSkill> updatedSkills = employeeService.updateGameSkills(employeeId, gameSkills);
            return ApiResponse.success("游戏技能更新成功", updatedSkills)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("更新游戏技能失败: {}", e.getMessage());
            return ApiResponse.<List<GameSkill>>error(400, e.getMessage())
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }
}
