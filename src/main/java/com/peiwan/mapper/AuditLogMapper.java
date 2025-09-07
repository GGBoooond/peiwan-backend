package com.peiwan.mapper;

import com.peiwan.entity.AuditLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 审核日志Mapper接口
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Mapper
public interface AuditLogMapper {

    /**
     * 插入审核日志
     */
    @Insert("INSERT INTO audit_logs (order_id, auditor_id, action, comments, created_at, updated_at, deleted) " +
            "VALUES (#{orderId}, #{auditorId}, #{action}, #{comments}, #{createdAt}, #{updatedAt}, #{deleted})")
    @org.apache.ibatis.annotations.Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AuditLog log);

    /**
     * 根据ID查询审核日志
     */
    @Select("SELECT * FROM audit_logs WHERE id = #{id} AND deleted = 0")
    AuditLog selectById(@Param("id") Long id);

    /**
     * 根据ID更新审核日志
     */
    @Update("UPDATE audit_logs SET order_id = #{orderId}, auditor_id = #{auditorId}, " +
            "action = #{action}, comments = #{comments}, updated_at = #{updatedAt} WHERE id = #{id}")
    int updateById(AuditLog log);

    /**
     * 根据ID删除审核日志（逻辑删除）
     */
    @Update("UPDATE audit_logs SET deleted = 1, updated_at = NOW() WHERE id = #{id}")
    int deleteById(@Param("id") Long id);

    /**
     * 根据工单ID查找审核日志
     */
    @Select("SELECT * FROM audit_logs WHERE order_id = #{orderId} AND deleted = 0 ORDER BY created_at DESC")
    List<AuditLog> findByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据审核人ID查找审核日志
     */
    @Select("SELECT * FROM audit_logs WHERE auditor_id = #{auditorId} AND deleted = 0 ORDER BY created_at DESC")
    List<AuditLog> findByAuditorId(@Param("auditorId") Long auditorId);
}

