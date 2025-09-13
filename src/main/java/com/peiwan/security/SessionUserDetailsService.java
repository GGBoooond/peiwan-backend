package com.peiwan.security;

import com.peiwan.entity.User;
import com.peiwan.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

/**
 * 用户详情服务
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@Service
@RequiredArgsConstructor
public class SessionUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        return new SessionUserDetails(user);
    }

    /**
     * 根据用户ID加载用户详情
     */
    public UserDetails loadUserByUserId(Long userId) throws UsernameNotFoundException {
        User user = userService.findById(userId);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + userId);
        }

        return new SessionUserDetails(user);
    }

    /**
     * Session用户详情实现
     */
    public static class SessionUserDetails implements UserDetails {
        private final User user;

        public SessionUserDetails(User user) {
            this.user = user;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            // 根据用户角色返回权限
            return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
            );
        }

        @Override
        public String getPassword() {
            return user.getPasswordHash();
        }

        @Override
        public String getUsername() {
            return user.getUsername();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return user.getIsActive();
        }

        public User getUser() {
            return user;
        }

        public Long getUserId() {
            return user.getId();
        }

        public User.UserRole getUserRole() {
            return user.getRole();
        }
    }
}
