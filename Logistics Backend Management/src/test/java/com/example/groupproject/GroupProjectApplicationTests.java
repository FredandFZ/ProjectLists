package com.example.groupproject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.mindrot.jbcrypt.BCrypt;

@SpringBootTest
class GroupProjectApplicationTests {

    @Test
    public void encodePassword(){
        String password = "123456";

        // 加密
        String encodedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println(encodedPassword);

        // 使用正确密码验证密码是否正确
        boolean flag = BCrypt.checkpw(password, encodedPassword);
        System.out.println(flag);

        // 使用错误密码验证密码是否正确
        flag = BCrypt.checkpw("111222", encodedPassword);
        System.out.println(flag);
    }
}
