package com.study.usercenter.model.request;


import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author HH
 * @version 1.0
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -3137736605580668119L;

    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String planetCode;

}
