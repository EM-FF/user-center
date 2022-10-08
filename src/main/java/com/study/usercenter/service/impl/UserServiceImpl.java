package com.study.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.usercenter.common.BaseResponse;
import com.study.usercenter.common.ErrorCode;
import com.study.usercenter.exception.BusinessException;
import com.study.usercenter.model.domain.User;
import com.study.usercenter.service.UserService;
import com.study.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.study.usercenter.constant.UserConstant.USER_LOGIN_STATUS;

/**
 * @author 86158
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2022-09-29 12:59:21
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    UserMapper userMapper;
    /**
     * 盐值，混淆密码
     */
    private final String SALT = "abc";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        // 1.校验 各字段非空
        if (StringUtils.isAllBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "参数为空");
        }
        //账户长度 **不小于** 4 位
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "用户账号过短");
        }
        //密码就 **不小于** 8 位
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "用户密码过短");
        }
        if (planetCode.length() > 5) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "星球编号过长");
        }
        //账户不包含特殊字符
        String specialPattern = "[\\u00A0\\s\"`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“'。，、？]";
        Matcher matcher = Pattern.compile(specialPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "账户包含特殊字符");
        }
        //密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "密码不一致");
        }

        //账户不能重复
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("userAccount", userAccount);
        long count = userMapper.selectCount(query);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "账号重复");
        }
        //星球编号不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode", planetCode);
        Long count1 = userMapper.selectCount(queryWrapper);
        if (count1 > 0) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "星球编号重复");
        }
        // 2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 3.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "保存失败");
        }
        return user.getId();
    }


    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 校验 各字段非空
        if (StringUtils.isAllBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "参数为空");
        }
        //账户长度 **不小于** 4 位
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "用户账号过短");
        }
        //密码就 **不小于** 8 位
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "用户密码过短");
        }
        //账户不包含特殊字符
        String specialPattern = "[\\u00A0\\s\"`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“'。，、？]";
        Matcher matcher = Pattern.compile(specialPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "账户包含特殊字符");
        }
        // 2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("userAccount", userAccount);
        query.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(query);
        if (user == null) {
            log.info("user login failed,userAccount can't match userPassword");
            throw new BusinessException(ErrorCode.NULL_ERROR, "请求数据为空");
        }

        // 3.脱敏
        User safetyUser = getSafetyUser(user);

        // 4.记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATUS, safetyUser);

        return safetyUser;

    }

    /**
     * 用户信息脱敏
     *
     * @param originalUser
     * @return
     */
    @Override
    public User getSafetyUser(User originalUser) {
        if (originalUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originalUser.getId());
        safetyUser.setUsername(originalUser.getUsername());
        safetyUser.setUserAccount(originalUser.getUserAccount());
        safetyUser.setAvatarUrl(originalUser.getAvatarUrl());
        safetyUser.setGender(originalUser.getGender());
        safetyUser.setPhone(originalUser.getPhone());
        safetyUser.setEmail(originalUser.getEmail());
        safetyUser.setUserRole(originalUser.getUserRole());
        safetyUser.setPlanetCode(originalUser.getPlanetCode());
        safetyUser.setUserStatus(originalUser.getUserStatus());
        safetyUser.setUpdateTime(originalUser.getUpdateTime());

        return safetyUser;
    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATUS);
        return 1;
    }
}




