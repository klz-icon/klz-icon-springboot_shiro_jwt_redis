package com.klz.iblog;

import com.klz.iblog.mapper.UserMapper;
import com.klz.iblog.util.JJWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class IblogApplicationTests {

    @Autowired
    JJWTUtil jjwtUtil;

    @Autowired
    UserMapper userMapper;

    @Test
    void contextLoads() {
//        System.out.println(jjwtUtil.secret);
//        String token = jjwtUtil.createToken("klz");
//        System.out.println(token);
    }

    @Test
    void test1(){
//        JJWTUtil jjwtUtil = new JJWTUtil();
        long curentTime = System.currentTimeMillis();
        String token = jjwtUtil.createToken("klz",curentTime);
        System.out.println(token);
        String username = jjwtUtil.getUsername(token);
        System.out.println(username);
        boolean flag = jjwtUtil.verify(token);
        System.out.println(flag);
    }

}
