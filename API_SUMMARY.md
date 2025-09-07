# 陪玩系统后端API接口总结

## 系统概述

基于Spring Boot 3.2.0的陪玩业务管理系统后端，提供完整的RESTful API接口，支持管理员、客服、员工三种角色的业务操作。

## 技术架构

- **框架**: Spring Boot 3.2.0
- **安全**: Spring Security + JWT
- **数据库**: MySQL 8.0 + MyBatis Plus
- **文档**: Knife4j (Swagger 3)
- **连接池**: Druid
- **构建**: Maven

## 核心功能模块

### 1. 认证模块 (`/auth`)
- `POST /auth/login` - 用户登录
- `POST /auth/register` - 用户注册
- `POST /auth/logout` - 用户登出
- `GET /auth/me` - 获取当前用户信息
- `GET /auth/check-username/{username}` - 检查用户名可用性

### 2. 管理员模块 (`/admin`)
- `GET /admin/users` - 获取用户列表
- `POST /admin/users` - 创建用户
- `PUT /admin/users/{userId}` - 更新用户信息
- `DELETE /admin/users/{userId}` - 删除用户
- `GET /admin/orders` - 获取所有工单

### 3. 客服模块 (`/cs`)
- `GET /cs/employees` - 获取管理的员工列表
- `POST /cs/orders` - 创建工单
- `GET /cs/orders` - 获取派发的工单
- `POST /cs/orders/{orderId}/audit` - 审核工单

### 4. 员工模块 (`/employee`)
- `GET /employee/profile` - 获取个人资料
- `PUT /employee/profile` - 更新个人资料
- `GET /employee/orders` - 获取分配的工单
- `POST /employee/orders/{orderId}/accept` - 接单
- `POST /employee/orders/{orderId}/complete` - 完成订单
- `POST /employee/orders/{orderId}/renew` - 发起续单

### 5. 文件上传模块 (`/upload`)
- `POST /upload/image` - 上传图片文件

## 数据模型

### 核心实体
1. **User** - 用户表（管理员、客服、员工）
2. **EmployeeProfile** - 员工资料表
3. **GameSkill** - 游戏技能表
4. **CsEmployeeMapping** - 客服员工关系表
5. **Order** - 工单表
6. **OrderProof** - 工单凭证表
7. **AuditLog** - 审核日志表

### 工单状态流转
```
PENDING_ACCEPTANCE → IN_PROGRESS → PENDING_AUDIT → COMPLETED/REJECTED
     (待接单)         (进行中)       (待审核)        (已结单/未通过)
```

## 业务流程

### 工单生命周期
1. **客服创建工单** → 状态：待接单
2. **员工接单** → 状态：进行中
3. **员工完成** → 状态：待审核
4. **客服审核** → 状态：已结单/未通过
5. **员工续单** → 创建新工单

### 权限控制
- **管理员**: 用户管理、查看所有数据
- **客服**: 管理指定员工、创建审核工单
- **员工**: 个人资料管理、工单操作

## 安全机制

### JWT认证
- 访问令牌有效期：2小时
- 刷新令牌有效期：7天
- 自动令牌刷新机制

### 权限验证
- 基于角色的访问控制(RBAC)
- 接口级别的权限验证
- 数据级别的权限隔离

## 响应格式

### 统一响应结构
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": "2024-01-01T12:00:00Z",
  "requestId": "req_123456"
}
```

### 错误处理
- 400: 请求参数错误
- 401: 未授权
- 403: 权限不足
- 404: 资源不存在
- 500: 服务器内部错误

## 部署信息

### 开发环境
- 端口: 8080
- 数据库: MySQL 8.0
- API文档: http://localhost:8080/api/doc.html
- 数据库监控: http://localhost:8080/api/druid

### 默认账号
- 管理员: admin / admin123
- 客服: cs001 / admin123
- 员工: emp001 / admin123

## 开发规范

### 代码结构
```
src/main/java/com/peiwan/
├── config/          # 配置类
├── controller/      # 控制器
├── dto/            # 数据传输对象
├── entity/         # 实体类
├── exception/      # 异常处理
├── mapper/         # MyBatis映射器
├── security/       # 安全相关
├── service/        # 服务层
└── util/           # 工具类
```

### 命名规范
- 类名：大驼峰命名
- 方法名：小驼峰命名
- 常量：全大写加下划线
- 数据库字段：下划线命名

### 注释规范
- 类和方法必须有JavaDoc注释
- 复杂业务逻辑需要行内注释
- API接口必须有Swagger注解

## 性能优化

### 数据库优化
- 使用Druid连接池
- 合理的索引设计
- 分页查询支持

### 缓存策略
- 用户信息缓存
- 配置信息缓存
- 查询结果缓存

### 异步处理
- 文件上传异步处理
- 日志记录异步处理
- 通知消息异步发送

## 监控和日志

### 日志配置
- 应用日志：logs/peiwan-backend.log
- 错误日志：logs/error.log
- 访问日志：logs/access.log

### 监控指标
- 接口响应时间
- 数据库连接状态
- 系统资源使用情况
- 业务指标统计

## 扩展性设计

### 模块化架构
- 服务层接口化设计
- 配置外部化
- 插件化扩展

### 微服务准备
- 服务拆分友好
- 接口标准化
- 数据隔离设计

## 总结

本系统提供了完整的陪玩业务管理解决方案，包括：

1. **完整的用户权限体系**
2. **灵活的工单管理流程**
3. **安全的文件上传机制**
4. **完善的异常处理**
5. **详细的API文档**
6. **良好的扩展性设计**

系统设计遵循RESTful API规范，具有良好的可维护性和扩展性，能够满足陪玩业务的各种管理需求。

