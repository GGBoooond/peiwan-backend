package com.peiwan.util;

import cn.hutool.crypto.digest.BCrypt;

/**
 * 密码生成工具类
 * 
 * @author peiwan
 * @since 2024-01-01
 */
public class PasswordGenerator {

    public static void main(String[] args) {
        String password = "1234";
        
        // 生成多个哈希值来演示每次都是不同的
        System.out.println("密码: " + password);
        System.out.println("生成的BCrypt哈希值:");
        
        for (int i = 1; i <= 5; i++) {
            String hash = BCrypt.hashpw(password, BCrypt.gensalt());
            System.out.println(i + ". " + hash);
        }
        
        // 验证密码
        System.out.println("\n验证密码:");
//        String testHash = BCrypt.hashpw(password, BCrypt.gensalt());
        String testHash ="$2a$10$Q4ZDp/WqQXlAaO3p3zAwa.5TKbC7T4b5H5wYzgb7S0h3AOVMO7N.y";
        System.out.println("哈希值: " + testHash);
        System.out.println("验证结果: " + BCrypt.checkpw(password, testHash));
        System.out.println("错误密码验证: " + BCrypt.checkpw("12345", testHash));
    }
}
