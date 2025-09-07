package com.peiwan.mapper;

import com.peiwan.entity.CsEmployeeMapping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 客服员工关系Mapper接口
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Mapper
public interface CsEmployeeMappingMapper {

    /**
     * 插入客服员工关系
     */
    @Insert("INSERT INTO cs_employee_mappings (cs_user_id, employee_user_id, created_at, updated_at, deleted) " +
            "VALUES (#{csUserId}, #{employeeUserId}, #{createdAt}, #{updatedAt}, #{deleted})")
    @org.apache.ibatis.annotations.Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CsEmployeeMapping mapping);

    /**
     * 根据ID查询客服员工关系
     */
    @Select("SELECT * FROM cs_employee_mappings WHERE id = #{id} AND deleted = 0")
    CsEmployeeMapping selectById(@Param("id") Long id);

    /**
     * 根据ID更新客服员工关系
     */
    @Update("UPDATE cs_employee_mappings SET cs_user_id = #{csUserId}, employee_user_id = #{employeeUserId}, " +
            "updated_at = #{updatedAt} WHERE id = #{id}")
    int updateById(CsEmployeeMapping mapping);

    /**
     * 根据ID删除客服员工关系（逻辑删除）
     */
    @Update("UPDATE cs_employee_mappings SET deleted = 1, updated_at = NOW() WHERE id = #{id}")
    int deleteById(@Param("id") Long id);

    /**
     * 根据客服ID查找管理的员工关系
     */
    @Select("SELECT * FROM cs_employee_mappings WHERE cs_user_id = #{csUserId} AND deleted = 0")
    List<CsEmployeeMapping> findByCsUserId(@Param("csUserId") Long csUserId);

    /**
     * 根据员工ID查找所属的客服关系
     */
    @Select("SELECT * FROM cs_employee_mappings WHERE employee_user_id = #{employeeUserId} AND deleted = 0")
    List<CsEmployeeMapping> findByEmployeeUserId(@Param("employeeUserId") Long employeeUserId);

    /**
     * 查询特定客服员工关系是否存在
     */
    @Select("SELECT * FROM cs_employee_mappings WHERE cs_user_id = #{csUserId} AND employee_user_id = #{employeeUserId} AND deleted = 0")
    CsEmployeeMapping findByCsAndEmployee(@Param("csUserId") Long csUserId, @Param("employeeUserId") Long employeeUserId);

    /**
     * 获取所有有效的客服员工关系
     */
    @Select("SELECT * FROM cs_employee_mappings WHERE deleted = 0 ORDER BY created_at DESC")
    List<CsEmployeeMapping> findAll();

    /**
     * 根据客服ID获取管理的员工用户ID列表
     */
    @Select("SELECT employee_user_id FROM cs_employee_mappings WHERE cs_user_id = #{csUserId} AND deleted = 0")
    List<Long> findEmployeeUserIdsByCsUserId(@Param("csUserId") Long csUserId);

    /**
     * 批量删除客服员工关系
     */
    @Update("UPDATE cs_employee_mappings SET deleted = 1, updated_at = NOW() WHERE cs_user_id = #{csUserId} AND deleted = 0")
    int deleteByCsUserId(@Param("csUserId") Long csUserId);

    /**
     * 批量删除员工的所有关系
     */
    @Update("UPDATE cs_employee_mappings SET deleted = 1, updated_at = NOW() WHERE employee_user_id = #{employeeUserId} AND deleted = 0")
    int deleteByEmployeeUserId(@Param("employeeUserId") Long employeeUserId);
}
