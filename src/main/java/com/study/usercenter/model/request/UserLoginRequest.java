package com.study.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HH
 * @version 1.0
 */
@Data
public class UserLoginRequest implements Serializable {


    private static final long serialVersionUID = 3146421645694371012L;

    private String userAccount;

    private String userPassword;
}
