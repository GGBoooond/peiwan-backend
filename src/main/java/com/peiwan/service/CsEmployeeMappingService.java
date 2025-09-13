package com.peiwan.service;

import com.peiwan.entity.CsEmployeeMapping;

import java.util.List;

/**
 * 客服员工关系服务接口
 * 
 * @author peiwan
 * @since 2024-01-01
 */
public interface CsEmployeeMappingService {

    /**
     * 创建客服员工关系
     */
    CsEmployeeMapping createMapping(Long csUserId, Long employeeUserId);

    /**
     * 根据ID查询客服员工关系
     */
    CsEmployeeMapping findById(Long id);

    /**
     * 根据客服ID查找管理的员工关系
     */
    List<CsEmployeeMapping> findByCsUserId(Long csUserId);

    /**
     * 根据员工ID查找所属的客服关系
     */
    List<CsEmployeeMapping> findByEmployeeUserId(Long employeeUserId);

    /**
     * 查询特定客服员工关系是否存在
     */
    CsEmployeeMapping findByCsAndEmployee(Long csUserId, Long employeeUserId);

    /**
     * 获取所有有效的客服员工关系
     */
    List<CsEmployeeMapping> findAll();

    /**
     * 根据客服ID获取管理的员工用户ID列表
     */
    List<Long> getEmployeeUserIdsByCsUserId(Long csUserId);

    /**
     * 更新客服员工关系
     */
    CsEmployeeMapping updateMapping(Long id, Long csUserId, Long employeeUserId);

    /**
     * 删除客服员工关系
     */
    void deleteMapping(Long id);

    /**
     * 删除客服的所有员工关系
     */
    void deleteByCsUserId(Long csUserId);

    /**
     * 删除员工的所有关系
     */
    void deleteByEmployeeUserId(Long employeeUserId);

    /**
     * 批量创建客服员工关系
     */
    List<CsEmployeeMapping> createMappings(Long csUserId, List<Long> employeeUserIds);

    /**
     * 重新分配员工给客服
     */
    void reassignEmployee(Long employeeUserId, Long newCsUserId);

    /**
     * 检查客服是否管理该员工
     */
    boolean isCsManageEmployee(Long csUserId, Long employeeUserId);
}

