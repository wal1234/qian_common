package com.qian.common.security;

import com.qian.common.domain.LoginUser;

/**
 * 安全服务接口
 */
public interface SecurityService {
    /**
     * 获取用户
     */
    LoginUser getLoginUser();

    /**
     * 获取用户ID
     */
    Long getUserId();

    /**
     * 获取部门ID
     */
    Long getDeptId();

    /**
     * 获取用户账号
     */
    String getUsername();

    /**
     * 获取用户
     */
    LoginUser getLoginUser(String token);

    /**
     * 设置用户身份信息
     */
    void setLoginUser(LoginUser loginUser);

    /**
     * 删除用户身份信息
     */
    void deleteLoginUser(String token);

    /**
     * 验证用户是否具备某权限
     */
    boolean hasPermi(String permission);

    /**
     * 验证用户是否不具备某权限，与 hasPermi 逻辑相反
     */
    boolean lacksPermi(String permission);

    /**
     * 验证用户是否具有以下任意一个权限
     */
    boolean hasAnyPermi(String... permissions);

    /**
     * 判断用户是否拥有某个角色
     */
    boolean hasRole(String role);

    /**
     * 验证用户是否不具备某角色，与 isRole 逻辑相反
     */
    boolean lacksRole(String role);

    /**
     * 验证用户是否具有以下任意一个角色
     */
    boolean hasAnyRoles(String... roles);
} 