package com.peiwan.service;

import com.peiwan.dto.OrderAuditRequest;
import com.peiwan.dto.OrderCompleteRequest;
import com.peiwan.dto.OrderCreateRequest;
import com.peiwan.entity.Order;

import java.util.List;

/**
 * 工单服务接口
 * 
 * @author peiwan
 * @since 2024-01-01
 */
public interface OrderService {

    /**
     * 创建工单
     */
    Order createOrder(OrderCreateRequest request, Long csUserId);

    /**
     * 根据ID查找工单
     */
    Order findById(Long id);

    /**
     * 根据员工ID查找工单列表
     */
    List<Order> findByEmployeeId(Long employeeId);

    /**
     * 根据客服ID查找工单列表
     */
    List<Order> findByCsId(Long csId);

    /**
     * 根据状态查找工单列表
     */
    List<Order> findByStatus(Order.OrderStatus status);

    /**
     * 接单
     */
    Order acceptOrder(Long orderId, String imageUrl, Long employeeId);

    /**
     * 完成订单
     */
    Order completeOrder(Long orderId, OrderCompleteRequest request, Long employeeId);

    /**
     * 审核工单
     */
    Order auditOrder(Long orderId, OrderAuditRequest request, Long auditorId);

    /**
     * 续单
     */
    Order renewOrder(Long orderId, Long employeeId);

    /**
     * 生成工单编号
     */
    String generateOrderNumber();
}

