package com.peiwan.mapper;

import com.peiwan.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 工单Mapper接口
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Mapper
public interface OrderMapper {

    /**
     * 插入工单
     */
    @Insert("INSERT INTO orders (order_number, client_info, status, assigned_employee_id, created_by_cs_id, " +
            "order_info_screenshot_url, acceptance_screenshot_url, completion_screenshot_url, accepted_at, completed_at, created_at, updated_at, deleted) " +
            "VALUES (#{orderNumber}, #{clientInfo}, #{status}, #{assignedEmployeeId}, #{createdByCsId}, " +
            "#{orderInfoScreenshotUrl}, #{acceptanceScreenshotUrl}, #{completionScreenshotUrl}, #{acceptedAt}, #{completedAt}, #{createdAt}, #{updatedAt}, #{deleted})")
    @org.apache.ibatis.annotations.Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Order order);

    /**
     * 根据ID查询工单
     */
    @Select("SELECT * FROM orders WHERE id = #{id} AND deleted = 0")
    Order selectById(@Param("id") Long id);

    /**
     * 根据ID更新工单
     */
    @Update("UPDATE orders SET order_number = #{orderNumber}, client_info = #{clientInfo}, " +
            "status = #{status}, assigned_employee_id = #{assignedEmployeeId}, " +
            "order_info_screenshot_url = #{orderInfoScreenshotUrl}, acceptance_screenshot_url = #{acceptanceScreenshotUrl}, " +
            "completion_screenshot_url = #{completionScreenshotUrl}, accepted_at = #{acceptedAt}, " +
            "completed_at = #{completedAt}, updated_at = #{updatedAt} WHERE id = #{id}")
    int updateById(Order order);

    /**
     * 根据ID删除工单（逻辑删除）
     */
    @Update("UPDATE orders SET deleted = 1, updated_at = NOW() WHERE id = #{id}")
    int deleteById(@Param("id") Long id);

    /**
     * 根据员工ID查找工单列表
     */
    @Select("SELECT * FROM orders WHERE assigned_employee_id = #{employeeId} AND deleted = 0 ORDER BY created_at DESC")
    List<Order> findByEmployeeId(@Param("employeeId") Long employeeId);

    /**
     * 根据客服ID查找工单列表
     */
    @Select("SELECT * FROM orders WHERE created_by_cs_id = #{csId} AND deleted = 0 ORDER BY created_at DESC")
    List<Order> findByCsId(@Param("csId") Long csId);

    /**
     * 根据状态查找工单列表
     */
    @Select("SELECT * FROM orders WHERE status = #{status} AND deleted = 0 ORDER BY created_at DESC")
    List<Order> findByStatus(@Param("status") Order.OrderStatus status);

    /**
     * 查找所有工单（用于分页）
     */
    @Select("SELECT * FROM orders WHERE deleted = 0 ORDER BY created_at DESC")
    List<Order> selectAll();

    /**
     * 根据员工ID和状态查找工单
     */
    @Select("SELECT * FROM orders WHERE assigned_employee_id = #{employeeId} AND status = #{status} AND deleted = 0 ORDER BY created_at DESC")
    List<Order> findByEmployeeIdAndStatus(@Param("employeeId") Long employeeId, @Param("status") Order.OrderStatus status);
}

