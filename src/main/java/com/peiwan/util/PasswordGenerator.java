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
        String password = "123456";
        
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
        String testHash ="$2a$10$hzT0tiOxD.YJ5DNYhxjsX.b3xDYGM4nJEYe24a.y4SUXYV0XsMRbe";
        System.out.println("哈希值: " + testHash);
        System.out.println("验证结果: " + BCrypt.checkpw(password, testHash));
        System.out.println("错误密码验证: " + BCrypt.checkpw("123456", testHash));
    }
}
