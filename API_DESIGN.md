# 陪玩系统API设计文档

## 基础信息

- **基础URL**: `http://localhost:8080/api`
- **认证方式**: JWT Bearer Token
- **数据格式**: JSON

## 统一响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": "2024-01-01 12:00:00"
}
```

## 核心接口

### 认证接口

#### 用户登录
- **POST** `/auth/login`
- **请求体**: `{"username": "admin", "password": "admin123"}`
- **响应**: 返回JWT令牌和用户信息

#### 用户注册
- **POST** `/auth/register`
- **请求体**: `{"username": "newuser", "realName": "新用户", "password": "password123", "confirmPassword": "password123"}`

### 管理员接口

#### 获取用户列表
- **GET** `/admin/users`
- **权限**: ADMIN
- **查询参数**: page, size, role, keyword

#### 创建用户
- **POST** `/admin/users`
- **权限**: ADMIN
- **请求体**: 用户信息

### 客服接口

#### 获取管理的员工
- **GET** `/cs/employees`
- **权限**: CS
- **响应**: 员工列表及其状态

#### 创建工单
- **POST** `/cs/orders`
- **权限**: CS
- **请求体**: `{"assignedEmployeeId": 1, "clientInfo": "客户信息", "orderInfoScreenshotUrl": "图片URL"}`

#### 审核工单
- **POST** `/cs/orders/{orderId}/audit`
- **权限**: CS
- **请求体**: `{"action": "APPROVE", "comments": "审核意见"}`

### 员工接口

#### 获取个人资料
- **GET** `/employee/profile`
- **权限**: EMPLOYEE

#### 更新个人资料
- **PUT** `/employee/profile`
- **权限**: EMPLOYEE
- **请求体**: 个人资料信息

#### 获取分配的工单
- **GET** `/employee/orders`
- **权限**: EMPLOYEE

#### 接单
- **POST** `/employee/orders/{orderId}/accept`
- **权限**: EMPLOYEE
- **请求体**: `{"imageUrl": "接单截图URL"}`

#### 完成订单
- **POST** `/employee/orders/{orderId}/complete`
- **权限**: EMPLOYEE
- **请求体**: `{"imageUrl": "完成截图URL"}`

### 文件上传

#### 上传文件
- **POST** `/upload`
- **请求**: multipart/form-data
- **响应**: 返回文件URL

## 数据模型

### 用户
```json
{
  "id": 1,
  "username": "admin",
  "realName": "系统管理员",
  "role": "ADMIN",
  "isActive": true
}
```

### 工单
```json
{
  "id": 1,
  "orderNumber": "ORD20240101001",
  "clientInfo": "客户信息",
  "status": "PENDING_ACCEPTANCE",
  "assignedEmployeeId": 1,
  "createdByCsId": 2
}
```

## 状态码

- 200: 成功
- 400: 请求错误
- 401: 未授权
- 403: 禁止访问
- 404: 资源不存在
- 500: 服务器错误
