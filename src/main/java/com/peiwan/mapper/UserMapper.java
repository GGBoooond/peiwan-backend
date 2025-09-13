package com.peiwan.mapper;

import com.peiwan.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 用户Mapper接口
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Mapper
public interface UserMapper {

    /**
     * 插入用户
     */
    @Insert("INSERT INTO users (username, password_hash, real_name, phone, role, is_active, last_login, created_at, updated_at, deleted) " +
            "VALUES (#{username}, #{passwordHash}, #{realName}, #{phone}, #{role}, #{isActive}, #{lastLogin}, #{createdAt}, #{updatedAt}, #{deleted})")
    @org.apache.ibatis.annotations.Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    /**
     * 根据ID查询用户
     */
    @Select("SELECT * FROM users WHERE id = #{id} AND deleted = 0")
    User selectById(@Param("id") Long id);

    /**
     * 根据ID更新用户
     */
    @Update("UPDATE users SET username = #{username}, password_hash = #{passwordHash}, real_name = #{realName}, " +
            "phone = #{phone}, role = #{role}, is_active = #{isActive}, last_login = #{lastLogin}, updated_at = #{updatedAt} " +
            "WHERE id = #{id}")
    int updateById(User user);

    /**
     * 根据ID删除用户（逻辑删除）
     */
    @Update("UPDATE users SET deleted = 1, updated_at = NOW() WHERE id = #{id}")
    int deleteById(@Param("id") Long id);

    /**
     * 根据用户名查找用户
     */
    @Select("SELECT * FROM users WHERE username = #{username} AND deleted = 0")
    User findByUsername(@Param("username") String username);

    /**
     * 根据真实姓名查找用户
     */
    @Select("SELECT * FROM users WHERE real_name = #{realName} AND deleted = 0")
    User findByRealName(@Param("realName") String realName);

    /**
     * 根据手机号查找用户
     */
    @Select("SELECT * FROM users WHERE phone = #{phone} AND deleted = 0")
    User findByPhone(@Param("phone") String phone);

    /**
     * 根据角色查找用户列表
     */
    @Select("SELECT * FROM users WHERE role = #{role} AND deleted = 0 ORDER BY created_at DESC")
    List<User> findByRole(@Param("role") User.UserRole role);

    /**
     * 查找所有激活的用户
     */
    @Select("SELECT * FROM users WHERE is_active = 1 AND deleted = 0 ORDER BY created_at DESC")
    List<User> findAllActive();

    /**
     * 查找所有激活的用户（包含员工状态）
     */
    @Select("SELECT u.*, ep.work_status FROM users u " +
            "LEFT JOIN employee_profiles ep ON u.id = ep.user_id AND ep.deleted = 0 " +
            "WHERE u.is_active = 1 AND u.deleted = 0 ORDER BY u.created_at DESC")
    List<User> findAllActiveWithWorkStatus();
}

