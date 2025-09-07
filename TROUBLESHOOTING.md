# 陪玩系统后端故障排除指南

## 常见问题及解决方案

### 1. 依赖问题

#### 问题：Maven依赖下载失败
**症状**: 编译时出现依赖下载错误
**解决方案**:
```bash
# 清理Maven缓存
mvn clean
rm -rf ~/.m2/repository

# 重新下载依赖
mvn dependency:resolve
```

#### 问题：JPA与MyBatis Plus冲突
**症状**: 启动时出现Bean冲突错误
**解决方案**: 已移除JPA依赖，只使用MyBatis Plus

### 2. 数据库连接问题

#### 问题：MySQL连接失败
**症状**: 启动时出现数据库连接错误
**解决方案**:
1. 确保MySQL服务正在运行
2. 检查数据库连接配置
3. 确保数据库`peiwan_system`已创建
4. 执行数据库初始化脚本

```bash
# 创建数据库
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS peiwan_system;"

# 执行初始化脚本
mysql -u root -p peiwan_system < src/main/resources/db/init.sql
```

#### 问题：数据库表不存在
**症状**: 运行时出现表不存在的错误
**解决方案**: 执行数据库初始化脚本

### 3. JWT认证问题

#### 问题：JWT令牌验证失败
**症状**: 接口返回401未授权错误
**解决方案**:
1. 检查JWT配置是否正确
2. 确保请求头包含正确的Authorization格式
3. 检查令牌是否过期

#### 问题：用户信息获取失败
**症状**: 接口无法获取当前用户信息
**解决方案**: 检查JWT过滤器是否正确设置用户信息到请求头

### 4. 文件上传问题

#### 问题：文件上传失败
**症状**: 上传接口返回错误
**解决方案**:
1. 检查上传目录是否存在
2. 确保目录有写入权限
3. 检查文件大小是否超限

```bash
# 创建上传目录
mkdir -p uploads
chmod 755 uploads
```

#### 问题：文件路径配置错误
**症状**: 文件上传后无法访问
**解决方案**: 检查`application.yml`中的文件上传路径配置

### 5. 编译错误

#### 问题：Lombok注解不生效
**症状**: 编译时出现getter/setter方法找不到的错误
**解决方案**:
1. 确保IDE安装了Lombok插件
2. 确保Maven依赖中包含Lombok

#### 问题：验证注解不生效
**症状**: 参数验证不工作
**解决方案**: 确保添加了`@Valid`注解

### 6. 运行时错误

#### 问题：端口被占用
**症状**: 启动时出现端口8080被占用的错误
**解决方案**:
```bash
# 查找占用端口的进程
lsof -i :8080

# 杀死进程
kill -9 <PID>

# 或者修改配置文件中的端口
```

#### 问题：内存不足
**症状**: 启动时出现OutOfMemoryError
**解决方案**:
```bash
# 增加JVM内存
export MAVEN_OPTS="-Xmx1024m -Xms512m"
mvn spring-boot:run
```

### 7. 日志问题

#### 问题：日志文件无法创建
**症状**: 应用启动时出现日志文件创建失败
**解决方案**:
```bash
# 创建日志目录
mkdir -p logs
chmod 755 logs
```

### 8. 配置问题

#### 问题：配置文件读取失败
**症状**: 启动时出现配置读取错误
**解决方案**:
1. 检查`application.yml`文件格式是否正确
2. 确保YAML缩进正确
3. 检查配置项是否存在

### 9. 网络问题

#### 问题：跨域请求失败
**症状**: 前端请求被CORS阻止
**解决方案**: 检查Spring Security配置中的CORS设置

### 10. 性能问题

#### 问题：数据库查询慢
**症状**: 接口响应时间过长
**解决方案**:
1. 检查数据库索引
2. 优化SQL查询
3. 检查连接池配置

## 调试技巧

### 1. 启用调试日志
在`application.yml`中添加：
```yaml
logging:
  level:
    com.peiwan: debug
    org.springframework.security: debug
    org.springframework.web: debug
```

### 2. 使用Postman测试API
1. 先调用登录接口获取token
2. 在后续请求中添加Authorization头
3. 使用Bearer token格式

### 3. 查看应用日志
```bash
tail -f logs/peiwan-backend.log
```

### 4. 检查数据库状态
```bash
# 连接数据库
mysql -u root -p peiwan_system

# 查看表结构
SHOW TABLES;
DESCRIBE users;
```

## 启动检查清单

在启动应用前，请确保：

- [ ] Java 17+ 已安装
- [ ] Maven 3.6+ 已安装
- [ ] MySQL 8.0+ 正在运行
- [ ] 数据库`peiwan_system`已创建
- [ ] 数据库初始化脚本已执行
- [ ] 端口8080未被占用
- [ ] 上传目录和日志目录已创建
- [ ] 网络连接正常

## 联系支持

如果遇到其他问题，请：
1. 查看应用日志获取详细错误信息
2. 检查系统环境配置
3. 参考Spring Boot官方文档
4. 提交Issue到项目仓库

