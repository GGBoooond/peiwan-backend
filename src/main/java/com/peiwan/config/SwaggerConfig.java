package com.peiwan.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger配置类
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("陪玩系统API文档")
                        .description("陪玩业务管理系统后端API接口文档（基于Session认证）")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("陪玩系统开发团队")
                                .email("dev@peiwan.com")
                                .url("https://github.com/peiwan-system"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}

