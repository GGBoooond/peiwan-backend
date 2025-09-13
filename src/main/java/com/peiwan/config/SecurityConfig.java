package com.peiwan.config;

import com.peiwan.security.CustomPermissionEvaluator;
import com.peiwan.security.SessionAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final SessionAuthenticationFilter sessionAuthenticationFilter;
    private final CustomPermissionEvaluator permissionEvaluator;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)  // 禁用CSRF保护
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // 配置CORS
            .authorizeHttpRequests(authz -> authz
                // 公开接口（无需认证）
                .requestMatchers("/auth/login", "/auth/register", "/auth/check-username/**", "/auth/check-phone/**").permitAll()
                .requestMatchers("/doc.html", "/webjars/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
                .requestMatchers("/druid/**").permitAll()
                .requestMatchers("/upload/**").permitAll()  // 文件上传接口

                // 管理员接口 - 管理员可以访问任何接口
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // 客服接口 - 管理员和客服可以访问
                .requestMatchers("/cs/**").hasAnyRole("ADMIN", "CS")

                // 员工接口 - 管理员、客服和员工都可以访问
                .requestMatchers("/employee/**").hasAnyRole("ADMIN", "CS", "EMPLOYEE")

                // 其他接口需要认证
                .anyRequest().authenticated()
            )
            // 添加Session认证过滤器
            .addFilterBefore(sessionAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
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

    /**
     * 配置方法级权限表达式处理器
     */
    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        return expressionHandler;
    }
}

