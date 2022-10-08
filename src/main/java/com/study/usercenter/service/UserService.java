package com.study.usercenter.service;

import com.study.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author 86158
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2022-09-29 12:59:21
*/
public interface UserService extends IService<User> {



    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  账户密码
     * @param checkPassword 校验码
     * @param planetCode 星球id
     * @return              新用户id
     */
    long userRegister(String userAccount,String userPassword,String checkPassword,String planetCode);

    /**
     * 用户登录
     *
     * @param userAccount    用户账户
     * @param userPassword  账户密码
     * @param request
     * @return 返回脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     *
     * @param originalUser
     * @return
     */
    User getSafetyUser(User originalUser);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);
}
