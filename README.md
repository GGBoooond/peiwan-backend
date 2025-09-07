# 陪玩系统后端

基于Spring Boot的陪玩业务管理系统后端服务。

## 技术栈

- **框架**: Spring Boot 3.2.0
- **数据库**: MySQL 8.0
- **ORM**: MyBatis Plus 3.5.4.1
- **安全**: Spring Security + JWT
- **文档**: Knife4j (Swagger)
- **连接池**: Druid
- **工具**: Hutool
- **构建**: Maven

## 系统架构

### 核心模块

1. **用户管理模块**
   - 用户注册、登录、认证
   - 角色权限管理（管理员、客服、员工）
   - JWT令牌管理

2. **员工管理模块**
   - 员工资料管理
   - 游戏技能管理
   - 工作状态管理

3. **工单管理模块**
   - 工单创建、派发
   - 工单状态流转
   - 凭证上传管理
   - 审核流程管理

4. **客服管理模块**
   - 客服员工关系管理
   - 工单审核
   - 员工状态监控

### 数据库设计

- `users` - 用户表
- `employee_profiles` - 员工资料表
- `game_skills` - 游戏技能表
- `cs_employee_mappings` - 客服员工关系表
- `orders` - 工单表
- `order_proofs` - 工单凭证表
- `audit_logs` - 审核日志表

## 快速开始

### 环境要求

- JDK 17+
- MySQL 8.0+
- Maven 3.6+

### 安装步骤

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd peiwan-backend
   ```

2. **配置数据库**
   ```bash
   # 创建数据库
   mysql -u root -p < src/main/resources/db/init.sql
   ```

3. **修改配置**
   ```bash
   # 编辑 application.yml
   vim src/main/resources/application.yml
   ```
   
   修改数据库连接信息：
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/peiwan_system
       username: your_username
       password: your_password
   ```

4. **启动应用**
   ```bash
   mvn spring-boot:run
   ```

5. **访问接口文档**
   ```
   http://localhost:8080/api/doc.html
   ```

### 默认账号

- **管理员**: admin / admin123
- **客服**: cs001 / admin123
- **员工**: emp001 / admin123

## API接口

### 认证接口

- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/logout` - 用户登出
- `GET /api/auth/me` - 获取当前用户信息

### 管理员接口

- `GET /api/admin/users` - 获取用户列表
- `POST /api/admin/users` - 创建用户
- `PUT /api/admin/users/{id}` - 更新用户
- `DELETE /api/admin/users/{id}` - 删除用户
- `GET /api/admin/orders` - 获取所有工单

### 客服接口

- `GET /api/cs/employees` - 获取管理的员工列表
- `POST /api/cs/orders` - 创建工单
- `GET /api/cs/orders` - 获取派发的工单
- `POST /api/cs/orders/{id}/audit` - 审核工单

### 员工接口

- `GET /api/employee/profile` - 获取个人资料
- `PUT /api/employee/profile` - 更新个人资料
- `GET /api/employee/orders` - 获取分配的工单
- `POST /api/employee/orders/{id}/accept` - 接单
- `POST /api/employee/orders/{id}/complete` - 完成订单

## 业务流程

### 工单生命周期

1. **创建工单** - 客服创建工单并分配给员工
2. **接单** - 员工上传接单截图，工单状态变为"进行中"
3. **完成** - 员工上传完成截图，工单状态变为"待审核"
4. **审核** - 客服审核工单，通过或拒绝
5. **续单** - 员工可以对已完成的工单发起续单

### 权限控制

- **管理员**: 拥有所有权限，可以管理用户和查看所有数据
- **客服**: 管理指定的员工，创建和审核工单
- **员工**: 管理个人资料，执行工单操作

## 部署

### 开发环境

```bash
mvn spring-boot:run
```

### 生产环境

```bash
# 打包
mvn clean package

# 运行
java -jar target/peiwan-backend-1.0.0.jar
```

### Docker部署

```bash
# 构建镜像
docker build -t peiwan-backend .

# 运行容器
docker run -d -p 8080:8080 --name peiwan-backend peiwan-backend
```

## 监控

- **API文档**: http://localhost:8080/api/doc.html
- **数据库监控**: http://localhost:8080/api/druid
- **健康检查**: http://localhost:8080/api/actuator/health

## 开发指南

### 代码结构

```
src/main/java/com/peiwan/
├── config/          # 配置类
├── controller/      # 控制器
├── dto/            # 数据传输对象
├── entity/         # 实体类
├── mapper/         # MyBatis映射器
├── security/       # 安全相关
├── service/        # 服务层
└── util/           # 工具类
```

### 开发规范

1. **命名规范**
   - 类名使用大驼峰命名
   - 方法名使用小驼峰命名
   - 常量使用全大写加下划线

2. **注释规范**
   - 类和方法必须有JavaDoc注释
   - 复杂业务逻辑需要行内注释

3. **异常处理**
   - 统一使用全局异常处理器
   - 业务异常需要明确的错误信息

## 许可证

MIT License

