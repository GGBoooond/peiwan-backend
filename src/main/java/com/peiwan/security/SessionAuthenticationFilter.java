package com.peiwan.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Session认证过滤器
 * 从Session中获取用户信息并设置到Security Context中
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SessionAuthenticationFilter extends OncePerRequestFilter {

    private final SessionUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // 如果已经认证，跳过
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                filterChain.doFilter(request, response);
                return;
            }

            // 获取Session
            HttpSession session = request.getSession(false);
            if (session == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // 从Session中获取用户信息
            Long userId = (Long) session.getAttribute("userId");
            String userRole = (String) session.getAttribute("userRole");

            if (userId != null && userRole != null) {
                try {
                    // 加载用户详情
                    UserDetails userDetails = userDetailsService.loadUserByUserId(userId);
                    
                    // 创建认证token
                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(
                            userDetails, 
                            null, 
                            userDetails.getAuthorities()
                        );
                    
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // 设置到Security Context
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    log.debug("用户认证成功: userId={}, role={}", userId, userRole);
                } catch (UsernameNotFoundException e) {
                    log.warn("Session中的用户不存在: userId={}", userId);
                    // 清除无效的Session
                    session.invalidate();
                }
            }
        } catch (Exception e) {
            log.error("Session认证过滤器异常", e);
        }

        filterChain.doFilter(request, response);
    }
}
