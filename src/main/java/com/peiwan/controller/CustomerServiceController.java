package com.peiwan.controller;

import com.peiwan.dto.ApiResponse;
import com.peiwan.dto.OrderAuditRequest;
import com.peiwan.dto.OrderCreateRequest;
import com.peiwan.entity.EmployeeProfile;
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
 * 客服控制器
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/cs")
@Tag(name = "客服管理", description = "客服相关接口")
public class CustomerServiceController {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceController.class);

    private final EmployeeService employeeService;
    private final OrderService orderService;

    public CustomerServiceController(EmployeeService employeeService, OrderService orderService) {
        this.employeeService = employeeService;
        this.orderService = orderService;
    }

    @GetMapping("/employees")
    @Operation(summary = "获取管理的员工列表", description = "客服获取管理的员工列表")
    public ApiResponse<List<EmployeeProfile>> getEmployees(HttpServletRequest httpRequest) {
        try {
            // 从请求头获取客服ID
            String csUserIdStr = httpRequest.getHeader("X-User-Id");
            if (csUserIdStr == null) {
                return ApiResponse.<List<EmployeeProfile>>error(401, "未找到用户信息")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            Long csUserId = Long.parseLong(csUserIdStr);
            List<EmployeeProfile> employees = employeeService.findByCsUserId(csUserId);
            return ApiResponse.success("获取员工列表成功", employees)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("获取员工列表失败: {}", e.getMessage());
            return ApiResponse.<List<EmployeeProfile>>error(500, "获取员工列表失败")
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @PostMapping("/orders")
    @Operation(summary = "创建工单", description = "客服为员工创建工单")
    public ApiResponse<Order> createOrder(@RequestBody OrderCreateRequest request, HttpServletRequest httpRequest) {
        try {
            // 从请求头获取客服ID
            String csUserIdStr = httpRequest.getHeader("X-User-Id");
            if (csUserIdStr == null) {
                return ApiResponse.<Order>error(401, "未找到用户信息")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            Long csUserId = Long.parseLong(csUserIdStr);
            Order order = orderService.createOrder(request, csUserId);
            return ApiResponse.success("工单创建成功", order)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("创建工单失败: {}", e.getMessage());
            return ApiResponse.<Order>error(400, e.getMessage())
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @GetMapping("/orders")
    @Operation(summary = "获取派发的工单", description = "客服获取派发的工单列表")
    public ApiResponse<List<Order>> getOrders(HttpServletRequest httpRequest) {
        try {
            // 从请求头获取客服ID
            String csUserIdStr = httpRequest.getHeader("X-User-Id");
            if (csUserIdStr == null) {
                return ApiResponse.<List<Order>>error(401, "未找到用户信息")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            Long csUserId = Long.parseLong(csUserIdStr);
            List<Order> orders = orderService.findByCsId(csUserId);
            return ApiResponse.success("获取工单列表成功", orders)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("获取工单列表失败: {}", e.getMessage());
            return ApiResponse.<List<Order>>error(500, "获取工单列表失败")
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }

    @PostMapping("/orders/{orderId}/audit")
    @Operation(summary = "审核工单", description = "客服审核工单")
    public ApiResponse<Order> auditOrder(@PathVariable Long orderId, @RequestBody OrderAuditRequest request, HttpServletRequest httpRequest) {
        try {
            // 从请求头获取审核人ID
            String auditorIdStr = httpRequest.getHeader("X-User-Id");
            if (auditorIdStr == null) {
                return ApiResponse.<Order>error(401, "未找到用户信息")
                        .requestId(httpRequest.getHeader("X-Request-Id"));
            }

            Long auditorId = Long.parseLong(auditorIdStr);
            Order order = orderService.auditOrder(orderId, request, auditorId);
            return ApiResponse.success("工单审核成功", order)
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        } catch (Exception e) {
            log.error("审核工单失败: {}", e.getMessage());
            return ApiResponse.<Order>error(400, e.getMessage())
                    .requestId(httpRequest.getHeader("X-Request-Id"));
        }
    }
}

