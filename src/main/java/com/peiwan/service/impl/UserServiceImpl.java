package com.peiwan.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.peiwan.dto.ChangePasswordRequest;
import com.peiwan.dto.LoginRequest;
import com.peiwan.dto.LoginResponse;
import com.peiwan.dto.RegisterRequest;
import com.peiwan.entity.User;
import com.peiwan.mapper.UserMapper;
import com.peiwan.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户服务实现类
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        // 查找用户
        User user = findByUsername(request.getUsername());
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        System.out.printf("第一次"+request.getPassword());
        System.out.printf("数据库"+user.getPasswordHash());
        System.out.printf("验证："+BCrypt.checkpw(request.getPassword(), user.getPasswordHash()));
        // 验证密码
        if (!BCrypt.checkpw(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 检查账户是否激活
        if (!user.getIsActive()) {
            throw new RuntimeException("账户已被禁用");
        }

        // 更新最后登录时间
        updateLastLogin(user.getId());

        // 构建响应（不再生成JWT令牌）
        LoginResponse response = new LoginResponse();
        response.setAccessToken("session-based"); // 使用session标识
        response.setRefreshToken("session-based");
        response.setExpiresIn(7200L); // 2小时

        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setRealName(user.getRealName());
        userInfo.setRole(user.getRole().name());
        userInfo.setRoleDescription(user.getRole().getDescription());
        userInfo.setCreatedAt(user.getCreatedAt());
        userInfo.setLastLogin(user.getLastLogin());

        response.setUser(userInfo);

        return response;
    }

    @Override
    @Transactional
    public User register(RegisterRequest request) {
        // 验证密码确认
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("两次输入的密码不一致");
        }

        // 检查用户名是否已存在
        User existingUser = findByUsername(request.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查手机号是否已存在（如果提供了手机号）
        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            User existingPhoneUser = findByPhone(request.getPhone());
            if (existingPhoneUser != null) {
                throw new RuntimeException("手机号已存在");
            }
        }

        // 验证用户名和真实姓名是否匹配（需要管理员预先录入）
        if (!validateUsernameAndRealName(request.getUsername(), request.getRealName())) {
            throw new RuntimeException("用户名或真实姓名不匹配，请联系管理员");
        }

        // 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setRealName(request.getRealName());
        user.setPhone(request.getPhone());
        user.setPasswordHash(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setRole(User.UserRole.EMPLOYEE); // 默认为员工角色
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userMapper.insert(user);

        log.info("用户注册成功: {}", user.getUsername());
        return user;
    }

    @Override
    public User findById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public User findByUsername(String username) {
        if (StrUtil.isBlank(username)) {
            return null;
        }
        return userMapper.findByUsername(username);
    }

    @Override
    public User findByRealName(String realName) {
        if (StrUtil.isBlank(realName)) {
            return null;
        }
        return userMapper.findByRealName(realName);
    }

    @Override
    public User findByPhone(String phone) {
        if (StrUtil.isBlank(phone)) {
            return null;
        }
        return userMapper.findByPhone(phone);
    }

    @Override
    public List<User> findByRole(User.UserRole role) {
        return userMapper.findByRole(role);
    }

    @Override
    public List<User> findAllActive() {
        return userMapper.findAllActive();
    }

    @Override
    public List<User> findAllActiveWithWorkStatus() {
        return userMapper.findAllActiveWithWorkStatus();
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        User userNew = userMapper.selectById(user.getId());
        user.setPasswordHash(userNew.getPasswordHash());
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
        return user;
    }

    @Override
    public void createUser(User user){
        userMapper.insert(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userMapper.deleteById(id);
    }

    @Override
    public boolean validateUsernameAndRealName(String username, String realName) {
        // 这里需要实现验证逻辑
        // 可以查询一个预注册表，或者通过其他方式验证
        // 暂时返回true，实际项目中需要根据业务需求实现
        return true;
    }

    @Override
    @Transactional
    public void updateLastLogin(Long userId) {
        // 先查询用户信息，避免覆盖其他字段
        User user = userMapper.selectById(userId);
        if (user != null) {
            user.setLastLogin(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            userMapper.updateById(user);
        }
    }

    @Override
    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        // 验证新密码和确认密码是否一致
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("新密码和确认密码不一致");
        }

        // 查找用户
        User user = findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证当前密码
        if (!BCrypt.checkpw(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new RuntimeException("当前密码错误");
        }

        // 检查新密码是否与当前密码相同
        if (BCrypt.checkpw(request.getNewPassword(), user.getPasswordHash())) {
            throw new RuntimeException("新密码不能与当前密码相同");
        }

        // 更新密码
        user.setPasswordHash(BCrypt.hashpw(request.getNewPassword(), BCrypt.gensalt()));
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);

        log.info("用户密码修改成功: userId={}, username={}", userId, user.getUsername());
    }
}

