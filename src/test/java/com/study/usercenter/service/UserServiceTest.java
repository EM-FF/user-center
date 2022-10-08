package com.study.usercenter.service;

import com.study.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


/**
 * 用户测试
 * @author HH
 * @version 1.0
 */
@SpringBootTest
class UserServiceTest {

    @Resource
    UserService userService;

    @Test
    void testAddUser() {
        User user = new User();
        user.setUsername("fnhah");
        user.setUserAccount("123");
        user.setAvatarUrl("https://www.nitutu.com/touxiang/ktdmtx/149459.html");
        user.setGender(0);
        user.setUserPassword("456");
        user.setPhone("123");
        user.setEmail("456");
        boolean save = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(save);


    }

    @Test
    void userRegister() {
        String userAccount = "haha";
        String userPassword = "";
        String checkPassword = "123456789";
        String planetCode = "1";
        long result = userService.userRegister(userAccount,userPassword,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);

        userAccount = "ha";
        userPassword = "123456789";
        result = userService.userRegister(userAccount,userPassword,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);

        userAccount = "fnhah";
        result = userService.userRegister(userAccount,userPassword,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);

        userAccount = "fn hah";
        result = userService.userRegister(userAccount,userPassword,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);

        userAccount = "admin";
        userPassword = "12345678910";
        result = userService.userRegister(userAccount,userPassword,checkPassword,planetCode);
        Assertions.assertEquals(-1,result);

    }



}