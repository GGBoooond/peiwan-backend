#!/bin/bash

echo "=== 陪玩系统后端启动脚本 ==="

# 检查Java版本
echo "检查Java版本..."
java -version
if [ $? -ne 0 ]; then
    echo "错误: 未找到Java环境，请安装JDK 17+"
    exit 1
fi

# 检查Maven
echo "检查Maven..."
mvn -version
if [ $? -ne 0 ]; then
    echo "错误: 未找到Maven环境"
    exit 1
fi

# 检查MySQL连接
echo "检查MySQL连接..."
mysql -h localhost -u root -p123456 -e "SELECT 1;" > /dev/null 2>&1
if [ $? -ne 0 ]; then
    echo "警告: 无法连接到MySQL，请确保MySQL服务正在运行"
    echo "请检查以下配置:"
    echo "1. MySQL服务是否启动"
    echo "2. 数据库连接信息是否正确"
    echo "3. 数据库peiwan_system是否存在"
    read -p "是否继续启动? (y/n): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

# 创建必要的目录
echo "创建必要的目录..."
mkdir -p logs
mkdir -p uploads

# 清理并编译
echo "清理并编译项目..."
mvn clean compile

# 启动应用
echo "启动应用..."
mvn spring-boot:run

echo "应用启动完成！"
echo "API文档地址: http://localhost:8080/api/doc.html"
echo "数据库监控地址: http://localhost:8080/api/druid"

