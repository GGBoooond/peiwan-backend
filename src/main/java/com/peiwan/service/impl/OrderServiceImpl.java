package com.peiwan.service.impl;

import com.peiwan.dto.OrderAuditRequest;
import com.peiwan.dto.OrderCompleteRequest;
import com.peiwan.dto.OrderCreateRequest;
import com.peiwan.entity.AuditLog;
import com.peiwan.entity.Order;
import com.peiwan.entity.OrderProof;
import com.peiwan.mapper.AuditLogMapper;
import com.peiwan.mapper.OrderMapper;
import com.peiwan.mapper.OrderProofMapper;
import com.peiwan.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 工单服务实现类
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderMapper orderMapper;
    private final OrderProofMapper orderProofMapper;
    private final AuditLogMapper auditLogMapper;

    public OrderServiceImpl(OrderMapper orderMapper, OrderProofMapper orderProofMapper, AuditLogMapper auditLogMapper) {
        this.orderMapper = orderMapper;
        this.orderProofMapper = orderProofMapper;
        this.auditLogMapper = auditLogMapper;
    }

    private static final AtomicInteger orderCounter = new AtomicInteger(1);

    @Override
    @Transactional
    public Order createOrder(OrderCreateRequest request, Long csUserId) {
        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setClientInfo(request.getClientInfo());
        order.setStatus(Order.OrderStatus.PENDING_ACCEPTANCE);
        order.setAssignedEmployeeId(request.getAssignedEmployeeId());
        order.setCreatedByCsId(csUserId);
        order.setOrderInfoScreenshotUrl(request.getOrderInfoScreenshotUrl());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        orderMapper.insert(order);
        log.info("工单创建成功: {}", order.getOrderNumber());
        return order;
    }

    @Override
    public Order findById(Long id) {
        return orderMapper.selectById(id);
    }

    @Override
    public List<Order> findByEmployeeId(Long employeeId) {
        return orderMapper.findByEmployeeId(employeeId);
    }

    @Override
    public List<Order> findByCsId(Long csId) {
        return orderMapper.findByCsId(csId);
    }

    @Override
    public List<Order> findByCsIdAndEmployeeId(Long csId, Long employeeId) {
        return orderMapper.findByCsIdAndEmployeeId(csId, employeeId);
    }

    @Override
    public List<Order> findByStatus(Order.OrderStatus status) {
        return orderMapper.findByStatus(status);
    }

    @Override
    @Transactional
    public Order acceptOrder(Long orderId, String imageUrl, Long employeeId) {
        Order order = findById(orderId);
        if (order == null) {
            throw new RuntimeException("工单不存在");
        }

        if (!order.getAssignedEmployeeId().equals(employeeId)) {
            throw new RuntimeException("无权操作此工单");
        }

        if (order.getStatus() != Order.OrderStatus.PENDING_ACCEPTANCE) {
            throw new RuntimeException("工单状态不允许接单");
        }

        // 更新工单状态与接单截图
        order.setStatus(Order.OrderStatus.IN_PROGRESS);
        order.setAcceptedAt(LocalDateTime.now());
        order.setAcceptanceScreenshotUrl(imageUrl);
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateById(order);

        // 保存接单凭证
        OrderProof proof = new OrderProof();
        proof.setOrderId(orderId);
        proof.setProofType(OrderProof.ProofType.ACCEPTANCE);
        proof.setImageUrl(imageUrl);
        proof.setUploadedAt(LocalDateTime.now());
        proof.setCreatedAt(LocalDateTime.now());
        proof.setUpdatedAt(LocalDateTime.now());
        orderProofMapper.insert(proof);

        log.info("工单接单成功: {}", order.getOrderNumber());
        return order;
    }

    @Override
    @Transactional
    public Order completeOrder(Long orderId, OrderCompleteRequest request, Long employeeId) {
        Order order = findById(orderId);
        if (order == null) {
            throw new RuntimeException("工单不存在");
        }

        if (!order.getAssignedEmployeeId().equals(employeeId)) {
            throw new RuntimeException("无权操作此工单");
        }

        if (order.getStatus() != Order.OrderStatus.IN_PROGRESS) {
            throw new RuntimeException("工单状态不允许完成");
        }

        // 更新工单状态与完成截图
        order.setStatus(Order.OrderStatus.PENDING_AUDIT);
        order.setCompletedAt(LocalDateTime.now());
        order.setCompletionScreenshotUrl(request.getImageUrl());
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateById(order);

        // 保存完成凭证
        OrderProof proof = new OrderProof();
        proof.setOrderId(orderId);
        proof.setProofType(OrderProof.ProofType.COMPLETION);
        proof.setImageUrl(request.getImageUrl());
        proof.setUploadedAt(LocalDateTime.now());
        proof.setCreatedAt(LocalDateTime.now());
        proof.setUpdatedAt(LocalDateTime.now());
        orderProofMapper.insert(proof);

        log.info("工单完成成功: {}", order.getOrderNumber());
        return order;
    }

    @Override
    @Transactional
    public Order auditOrder(Long orderId, OrderAuditRequest request, Long auditorId) {
        Order order = findById(orderId);
        if (order == null) {
            throw new RuntimeException("工单不存在");
        }

        if (order.getStatus() != Order.OrderStatus.PENDING_AUDIT && order.getStatus() != Order.OrderStatus.REJECTED_TO_SUBMIT) {
            throw new RuntimeException("工单状态不允许审核");
        }

        // 更新工单状态
        Order.OrderStatus newStatus = request.getAction() == AuditLog.AuditAction.APPROVE 
            ? Order.OrderStatus.COMPLETED 
            : Order.OrderStatus.REJECTED;
        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateById(order);

        // 保存审核日志
        AuditLog auditLog = new AuditLog();
        auditLog.setOrderId(orderId);
        auditLog.setAuditorId(auditorId);
        auditLog.setAction(request.getAction());
        auditLog.setComments(request.getComments());
        auditLog.setCreatedAt(LocalDateTime.now());
        auditLog.setUpdatedAt(LocalDateTime.now());
        auditLogMapper.insert(auditLog);

        log.info("工单审核成功: {} - {}", order.getOrderNumber(), request.getAction());
        return order;
    }

    @Override
    @Transactional
    public Order renewOrder(Long orderId, Long employeeId) {
        Order originalOrder = findById(orderId);
        if (originalOrder == null) {
            throw new RuntimeException("原工单不存在");
        }

        if (!originalOrder.getAssignedEmployeeId().equals(employeeId)) {
            throw new RuntimeException("无权操作此工单");
        }

        if (originalOrder.getStatus() != Order.OrderStatus.COMPLETED) {
            throw new RuntimeException("只有已完成的工单才能续单");
        }

        // 创建续单
        Order newOrder = new Order();
        newOrder.setOrderNumber(generateOrderNumber());
        newOrder.setClientInfo(originalOrder.getClientInfo());
        newOrder.setStatus(Order.OrderStatus.PENDING_ACCEPTANCE);
        newOrder.setOrderInfoScreenshotUrl(originalOrder.getOrderInfoScreenshotUrl());
        newOrder.setAssignedEmployeeId(employeeId);
        newOrder.setCreatedByCsId(originalOrder.getCreatedByCsId());
        newOrder.setCreatedAt(LocalDateTime.now());
        newOrder.setUpdatedAt(LocalDateTime.now());

        orderMapper.insert(newOrder);
        log.info("续单创建成功: {} -> {}", originalOrder.getOrderNumber(), newOrder.getOrderNumber());
        return newOrder;
    }

    @Override
    @Transactional
    public Order reSubmitOrder(Long orderId, OrderCompleteRequest request, Long employeeId) {
        Order originalOrder = findById(orderId);
        if (originalOrder == null) {
            throw new RuntimeException("原工单不存在");
        }

        if (!originalOrder.getAssignedEmployeeId().equals(employeeId)) {
            throw new RuntimeException("无权操作此工单");
        }

        if (originalOrder.getStatus() != Order.OrderStatus.REJECTED) {
            throw new RuntimeException("只有被拒绝的工单才能重新提单");
        }

        // 创建重提单
        Order newOrder = new Order();
        newOrder.setOrderNumber(generateOrderNumber());
        newOrder.setClientInfo(originalOrder.getClientInfo());
        newOrder.setStatus(Order.OrderStatus.REJECTED_TO_SUBMIT);
        newOrder.setOrderInfoScreenshotUrl(originalOrder.getOrderInfoScreenshotUrl());
        newOrder.setAcceptanceScreenshotUrl(originalOrder.getAcceptanceScreenshotUrl());
        newOrder.setCompletionScreenshotUrl(request.getImageUrl());
        newOrder.setAssignedEmployeeId(employeeId);
        newOrder.setCreatedByCsId(originalOrder.getCreatedByCsId());
        newOrder.setCreatedAt(LocalDateTime.now());
        newOrder.setUpdatedAt(LocalDateTime.now());

        orderMapper.insert(newOrder);
        // 保存完成凭证
        OrderProof proof = new OrderProof();
        proof.setOrderId(orderId);
        proof.setProofType(OrderProof.ProofType.COMPLETION);
        proof.setImageUrl(request.getImageUrl());
        proof.setUploadedAt(LocalDateTime.now());
        proof.setCreatedAt(LocalDateTime.now());
        proof.setUpdatedAt(LocalDateTime.now());
        orderProofMapper.insert(proof);
        log.info("重新提单创建成功: {} -> {}", originalOrder.getOrderNumber(), newOrder.getOrderNumber());
        return newOrder;
    }

    @Override
    public String generateOrderNumber() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int counter = orderCounter.getAndIncrement();
        return String.format("ORD%s%04d", date, counter);
    }
}

