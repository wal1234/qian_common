package com.qian.common.security;

import com.qian.common.domain.LoginUser;
import com.qian.common.utils.ServletUtils;
import com.qian.common.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * 安全服务实现类
 */
@Service("ss")
public class SecurityServiceImpl implements SecurityService {
    /**
     * 获取用户
     */
    @Override
    public LoginUser getLoginUser() {
        return ServletUtils.getLoginUser();
    }

    /**
     * 获取用户ID
     */
    @Override
    public Long getUserId() {
        return getLoginUser().getUserId();
    }

    /**
     * 获取部门ID
     */
    @Override
    public Long getDeptId() {
        return getLoginUser().getDeptId();
    }

    /**
     * 获取用户账号
     */
    @Override
    public String getUsername() {
        return getLoginUser().getUsername();
    }

    /**
     * 获取用户
     */
    @Override
    public LoginUser getLoginUser(String token) {
        return ServletUtils.getLoginUser(token);
    }

    /**
     * 设置用户身份信息
     */
    @Override
    public void setLoginUser(LoginUser loginUser) {
        ServletUtils.setLoginUser(loginUser);
    }

    /**
     * 删除用户身份信息
     */
    @Override
    public void deleteLoginUser(String token) {
        ServletUtils.deleteLoginUser(token);
    }

    /**
     * 验证用户是否具备某权限
     */
    @Override
    public boolean hasPermi(String permission) {
        if (StringUtils.isEmpty(permission)) {
            return false;
        }
        LoginUser loginUser = getLoginUser();
        if (loginUser == null || CollectionUtils.isEmpty(loginUser.getPermissions())) {
            return false;
        }
        return hasPermissions(loginUser.getPermissions(), permission);
    }

    /**
     * 验证用户是否不具备某权限，与 hasPermi 逻辑相反
     */
    @Override
    public boolean lacksPermi(String permission) {
        return !hasPermi(permission);
    }

    /**
     * 验证用户是否具有以下任意一个权限
     */
    @Override
    public boolean hasAnyPermi(String... permissions) {
        if (StringUtils.isEmpty(permissions)) {
            return false;
        }
        LoginUser loginUser = getLoginUser();
        if (loginUser == null || CollectionUtils.isEmpty(loginUser.getPermissions())) {
            return false;
        }
        Set<String> authorities = loginUser.getPermissions();
        for (String permission : permissions) {
            if (hasPermissions(authorities, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断用户是否拥有某个角色
     */
    @Override
    public boolean hasRole(String role) {
        if (StringUtils.isEmpty(role)) {
            return false;
        }
        LoginUser loginUser = getLoginUser();
        if (loginUser == null || CollectionUtils.isEmpty(loginUser.getPermissions())) {
            return false;
        }
        for (String permission : loginUser.getPermissions()) {
            if (role.equals(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证用户是否不具备某角色，与 isRole 逻辑相反
     */
    @Override
    public boolean lacksRole(String role) {
        return !hasRole(role);
    }

    /**
     * 验证用户是否具有以下任意一个角色
     */
    @Override
    public boolean hasAnyRoles(String... roles) {
        if (StringUtils.isEmpty(roles)) {
            return false;
        }
        LoginUser loginUser = getLoginUser();
        if (loginUser == null || CollectionUtils.isEmpty(loginUser.getPermissions())) {
            return false;
        }
        for (String role : roles) {
            if (hasRole(role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否包含权限
     */
    private boolean hasPermissions(Set<String> permissions, String permission) {
        return permissions.contains(permission);
    }
} 