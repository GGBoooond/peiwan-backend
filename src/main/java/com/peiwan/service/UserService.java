package com.peiwan.service;

import com.peiwan.dto.ChangePasswordRequest;
import com.peiwan.dto.LoginRequest;
import com.peiwan.dto.LoginResponse;
import com.peiwan.dto.RegisterRequest;
import com.peiwan.entity.User;

import java.util.List;

/**
 * 用户服务接口
 * 
 * @author peiwan
 * @since 2024-01-01
 */
public interface UserService {

    /**
     * 用户登录
     */
    LoginResponse login(LoginRequest request);

    /**
     * 用户注册
     */
    User register(RegisterRequest request);

    /**
     * 根据ID查找用户
     */
    User findById(Long id);

    /**
     * 根据用户名查找用户
     */
    User findByUsername(String username);

    /**
     * 根据真实姓名查找用户
     */
    User findByRealName(String realName);

    /**
     * 根据手机号查找用户
     */
    User findByPhone(String phone);

    /**
     * 根据角色查找用户列表
     */
    List<User> findByRole(User.UserRole role);

    /**
     * 查找所有激活的用户
     */
    List<User> findAllActive();

    /**
     * 查找所有激活的用户（包含员工状态）
     */
    List<User> findAllActiveWithWorkStatus();

    /**
     * 更新用户信息
     */
    User updateUser(User user);

    /**
     * 创建用户
     */
    void createUser(User user);

    /**
     * 删除用户
     */
    void deleteUser(Long id);

    /**
     * 验证用户名和真实姓名是否匹配
     */
    boolean validateUsernameAndRealName(String username, String realName);

    /**
     * 更新最后登录时间
     */
    void updateLastLogin(Long userId);

    /**
     * 修改用户密码
     */
    void changePassword(Long userId, ChangePasswordRequest request);
}

