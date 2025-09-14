-- 陪玩系统数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS peiwan_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE peiwan_system;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '游戏昵称（登录名）',
    password_hash VARCHAR(100) NOT NULL COMMENT '加密后的密码',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    phone VARCHAR(20) COMMENT '手机号',
    role ENUM('ADMIN', 'CS', 'EMPLOYEE') NOT NULL COMMENT '用户角色',
    is_active BOOLEAN NOT NULL DEFAULT TRUE COMMENT '账户是否激活',
    last_login DATETIME COMMENT '最后登录时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记'
) COMMENT '用户表';

-- 员工资料表
CREATE TABLE IF NOT EXISTS employee_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '员工资料ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    gender ENUM('MALE', 'FEMALE') COMMENT '性别',
    work_status ENUM('IDLE', 'BUSY', 'RESTING', 'OFF_DUTY') NOT NULL DEFAULT 'IDLE' COMMENT '工作状态',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',
    FOREIGN KEY (user_id) REFERENCES users(id)
) COMMENT '员工资料表';

-- 游戏技能表
CREATE TABLE IF NOT EXISTS game_skills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '游戏技能ID',
    profile_id BIGINT NOT NULL COMMENT '员工资料ID',
    game_name VARCHAR(100) NOT NULL COMMENT '游戏名称',
    play_style ENUM('TECHNICAL', 'ENTERTAINMENT') NOT NULL COMMENT '陪玩类型',
    service_type ENUM('RANKED', 'CASUAL') NOT NULL COMMENT '服务类型',
    highest_rank VARCHAR(100) COMMENT '最高段位',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',
    FOREIGN KEY (profile_id) REFERENCES employee_profiles(id)
) COMMENT '游戏技能表';

-- 客服员工关系表
CREATE TABLE IF NOT EXISTS cs_employee_mappings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '关系ID',
    cs_user_id BIGINT NOT NULL COMMENT '客服用户ID',
    employee_user_id BIGINT NOT NULL COMMENT '员工用户ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',
    FOREIGN KEY (cs_user_id) REFERENCES users(id),
    FOREIGN KEY (employee_user_id) REFERENCES users(id),
    UNIQUE KEY uk_cs_employee (cs_user_id, employee_user_id)
) COMMENT '客服员工关系表';

-- 工单表
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '工单ID',
    order_number VARCHAR(50) NOT NULL UNIQUE COMMENT '工单编号',
    client_info VARCHAR(500) NOT NULL COMMENT '委托人信息',
    status ENUM('PENDING_ACCEPTANCE', 'IN_PROGRESS', 'PENDING_AUDIT', 'COMPLETED', 'REJECTED') NOT NULL DEFAULT 'PENDING_ACCEPTANCE' COMMENT '工单状态',
    assigned_employee_id BIGINT NOT NULL COMMENT '分配的员工ID',
    created_by_cs_id BIGINT NOT NULL COMMENT '创建工单的客服ID',
    order_info_screenshot_url VARCHAR(500) COMMENT '派单信息截图URL',
    accepted_at DATETIME COMMENT '接单时间',
    completed_at DATETIME COMMENT '完成时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',
    FOREIGN KEY (assigned_employee_id) REFERENCES users(id),
    FOREIGN KEY (created_by_cs_id) REFERENCES users(id)
) COMMENT '工单表';

-- 同时添加两个截图URL字段
ALTER TABLE orders 
ADD COLUMN acceptance_screenshot_url VARCHAR(500) COMMENT '接单信息截图URL',
ADD COLUMN completion_screenshot_url VARCHAR(500) COMMENT '完成信息截图URL';

-- 添加手机号字段（如果不存在）
ALTER TABLE users 
ADD COLUMN phone VARCHAR(20) COMMENT '手机号' AFTER real_name;

-- 工单凭证表
CREATE TABLE IF NOT EXISTS order_proofs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '凭证ID',
    order_id BIGINT NOT NULL COMMENT '工单ID',
    proof_type ENUM('ACCEPTANCE', 'COMPLETION') NOT NULL COMMENT '凭证类型',
    image_url VARCHAR(500) NOT NULL COMMENT '图片URL',
    is_resubmission BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否为重新提交',
    is_renewal BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否为续单',
    uploaded_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',
    FOREIGN KEY (order_id) REFERENCES orders(id)
) COMMENT '工单凭证表';

-- 审核日志表
CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '审核日志ID',
    order_id BIGINT NOT NULL COMMENT '工单ID',
    auditor_id BIGINT NOT NULL COMMENT '审核人ID',
    action ENUM('APPROVE', 'REJECT') NOT NULL COMMENT '审核动作',
    comments VARCHAR(1000) COMMENT '审核意见',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记',
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (auditor_id) REFERENCES users(id)
) COMMENT '审核日志表';

-- 创建索引
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_deleted ON users(deleted);

CREATE INDEX idx_employee_profiles_user_id ON employee_profiles(user_id);
CREATE INDEX idx_employee_profiles_work_status ON employee_profiles(work_status);

CREATE INDEX idx_game_skills_profile_id ON game_skills(profile_id);

CREATE INDEX idx_cs_employee_mappings_cs_id ON cs_employee_mappings(cs_user_id);
CREATE INDEX idx_cs_employee_mappings_employee_id ON cs_employee_mappings(employee_user_id);

CREATE INDEX idx_orders_employee_id ON orders(assigned_employee_id);
CREATE INDEX idx_orders_cs_id ON orders(created_by_cs_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_number ON orders(order_number);

CREATE INDEX idx_order_proofs_order_id ON order_proofs(order_id);
CREATE INDEX idx_order_proofs_type ON order_proofs(proof_type);

CREATE INDEX idx_audit_logs_order_id ON audit_logs(order_id);
CREATE INDEX idx_audit_logs_auditor_id ON audit_logs(auditor_id);

-- 插入默认管理员账号
INSERT INTO users (username, password_hash, real_name, phone, role, is_active, created_at, updated_at) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '系统管理员', '13800138000', 'ADMIN', TRUE, NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- 插入示例数据（可选）
-- 示例客服
INSERT INTO users (username, password_hash, real_name, phone, role, is_active, created_at, updated_at) VALUES
('cs001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '客服小王', '13800138001', 'CS', TRUE, NOW(), NOW()),
('cs002', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '客服小李', '13800138002', 'CS', TRUE, NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- 示例员工
INSERT INTO users (username, password_hash, real_name, phone, role, is_active, created_at, updated_at) VALUES
('emp001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '员工张三', '13800138003', 'EMPLOYEE', TRUE, NOW(), NOW()),
('emp002', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '员工李四', '13800138004', 'EMPLOYEE', TRUE, NOW(), NOW()),
('emp003', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '员工王五', '13800138005', 'EMPLOYEE', TRUE, NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- 插入员工资料
INSERT INTO employee_profiles (user_id, gender, work_status, created_at, updated_at) VALUES
((SELECT id FROM users WHERE username = 'emp001'), 'MALE', 'IDLE', NOW(), NOW()),
((SELECT id FROM users WHERE username = 'emp002'), 'FEMALE', 'IDLE', NOW(), NOW()),
((SELECT id FROM users WHERE username = 'emp003'), 'MALE', 'IDLE', NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- 插入客服员工关系
INSERT INTO cs_employee_mappings (cs_user_id, employee_user_id, created_at, updated_at) VALUES
((SELECT id FROM users WHERE username = 'cs001'), (SELECT id FROM users WHERE username = 'emp001'), NOW(), NOW()),
((SELECT id FROM users WHERE username = 'cs001'), (SELECT id FROM users WHERE username = 'emp002'), NOW(), NOW()),
((SELECT id FROM users WHERE username = 'cs002'), (SELECT id FROM users WHERE username = 'emp003'), NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- 插入游戏技能
INSERT INTO game_skills (profile_id, game_name, play_style, service_type, highest_rank, created_at, updated_at) VALUES
((SELECT id FROM employee_profiles WHERE user_id = (SELECT id FROM users WHERE username = 'emp001')), '王者荣耀', 'TECHNICAL', 'RANKED', '王者50星', NOW(), NOW()),
((SELECT id FROM employee_profiles WHERE user_id = (SELECT id FROM users WHERE username = 'emp001')), '英雄联盟', 'ENTERTAINMENT', 'CASUAL', '钻石', NOW(), NOW()),
((SELECT id FROM employee_profiles WHERE user_id = (SELECT id FROM users WHERE username = 'emp002')), '王者荣耀', 'ENTERTAINMENT', 'CASUAL', '星耀', NOW(), NOW()),
((SELECT id FROM employee_profiles WHERE user_id = (SELECT id FROM users WHERE username = 'emp003')), '和平精英', 'TECHNICAL', 'RANKED', '王牌', NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();
