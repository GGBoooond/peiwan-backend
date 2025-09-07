package com.peiwan.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Spring Security配置类
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)  // 禁用CSRF保护
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // 配置CORS
            .authorizeHttpRequests(authz -> authz
                // 🚨 测试阶段：放开所有接口权限
                .anyRequest().permitAll()
                
                // 🔒 生产环境权限配置（注释掉，测试完成后启用）
                /*
                // 公开接口（无需认证）
                .requestMatchers("/auth/login", "/auth/register", "/auth/check-username/**").permitAll()
                .requestMatchers("/doc.html", "/webjars/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
                .requestMatchers("/druid/**").permitAll()
                .requestMatchers("/upload/**").permitAll()  // 文件上传接口
                
                // 管理员接口（需要ADMIN角色）
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // 客服接口（需要CS角色）
                .requestMatchers("/cs/**").hasRole("CS")
                
                // 员工接口（需要EMPLOYEE角色）
                .requestMatchers("/employee/**").hasRole("EMPLOYEE")
                
                // 其他接口需要认证
                .anyRequest().authenticated()
                */
            )
            .formLogin(AbstractHttpConfigurer::disable)  // 禁用表单登录
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/login")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));  // 允许所有来源
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

