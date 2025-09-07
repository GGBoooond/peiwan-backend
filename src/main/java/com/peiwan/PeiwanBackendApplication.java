package com.peiwan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 陪玩系统后端主启动类
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@SpringBootApplication
@MapperScan("com.peiwan.mapper")
@EnableCaching
@EnableAsync
@EnableTransactionManagement
public class PeiwanBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PeiwanBackendApplication.class, args);
        System.out.println("陪玩系统后端服务启动成功！");
        System.out.println("API文档地址: http://localhost:8080/api/doc.html");
        System.out.println("数据库监控地址: http://localhost:8080/api/druid");
    }
}

