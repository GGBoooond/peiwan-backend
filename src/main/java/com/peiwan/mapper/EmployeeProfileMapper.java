package com.peiwan.mapper;

import com.peiwan.entity.EmployeeProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 员工资料Mapper接口
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Mapper
public interface EmployeeProfileMapper {

    /**
     * 插入员工资料
     */
    @Insert("INSERT INTO employee_profiles (user_id, gender, work_status, created_at, updated_at, deleted) " +
            "VALUES (#{userId}, #{gender}, #{workStatus}, #{createdAt}, #{updatedAt}, #{deleted})")
    @org.apache.ibatis.annotations.Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(EmployeeProfile profile);

    /**
     * 根据ID查询员工资料
     */
    @Select("SELECT * FROM employee_profiles WHERE id = #{id} AND deleted = 0")
    EmployeeProfile selectById(@Param("id") Long id);

    /**
     * 根据ID更新员工资料
     */
    @Update("UPDATE employee_profiles SET user_id = #{userId}, gender = #{gender}, " +
            "work_status = #{workStatus}, updated_at = #{updatedAt} WHERE id = #{id}")
    int updateById(EmployeeProfile profile);

    /**
     * 根据ID删除员工资料（逻辑删除）
     */
    @Update("UPDATE employee_profiles SET deleted = 1, updated_at = NOW() WHERE id = #{id}")
    int deleteById(@Param("id") Long id);

    /**
     * 根据用户ID查找员工资料
     */
    @Select("SELECT * FROM employee_profiles WHERE user_id = #{userId} AND deleted = 0")
    EmployeeProfile findByUserId(@Param("userId") Long userId);

    /**
     * 根据工作状态查找员工
     */
    @Select("SELECT * FROM employee_profiles WHERE work_status = #{workStatus} AND deleted = 0")
    List<EmployeeProfile> findByWorkStatus(@Param("workStatus") EmployeeProfile.WorkStatus workStatus);

    /**
     * 根据客服ID查找管理的员工
     */
    @Select("SELECT ep.* FROM employee_profiles ep " +
            "INNER JOIN cs_employee_mappings cem ON ep.user_id = cem.employee_user_id " +
            "WHERE cem.cs_user_id = #{csUserId} AND ep.deleted = 0 AND cem.deleted = 0")
    List<EmployeeProfile> findByCsUserId(@Param("csUserId") Long csUserId);
}

