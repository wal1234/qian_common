package com.qian.common.enums.system;

import lombok.Getter;

@Getter
public enum PermissionEnum {
    USER_READ("user:read", "查看用户信息"),
    USER_WRITE("user:write", "修改用户信息"),
    USER_DELETE("user:delete", "删除用户信息"),
    ROLE_READ("role:read", "查看角色信息"),
    ROLE_WRITE("role:write", "修改角色信息"),
    ROLE_DELETE("role:delete", "删除角色信息");

    private final String code;
    private final String description;

    PermissionEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}