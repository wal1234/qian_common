package com.qian.common.constants;

import lombok.Getter;

/**
 * 结果状态码
 */
@Getter
public enum ResultCode {

    // 成功
    SUCCESS(200, "操作成功"),
    
    // 参数错误
    PARAM_ERROR(400, "参数错误"),
    PARAM_MISSING(401, "参数缺失"),
    PARAM_TYPE_ERROR(402, "参数类型错误"),
    PARAM_BINDING_ERROR(403, "参数绑定错误"),
    
    // 认证授权
    UNAUTHORIZED(401, "未授权的请求"),
    TOKEN_EXPIRE(401001, "令牌已过期"),
    TOKEN_INVALID(401002, "令牌无效"),
    TOKEN_MISSING(401003, "令牌缺失"),
    USER_LOGIN_ERROR(401004, "用户名或密码错误"),
    USER_DISABLED(401005, "用户已禁用"),
    
    // 权限不足
    FORBIDDEN(403, "权限不足"),
    
    // 资源不存在
    NOT_FOUND(404, "请求的资源不存在"),
    
    // 业务异常
    BUSINESS_ERROR(500001, "业务异常"),
    
    // 系统异常
    ERROR(500, "系统异常"),
    SYSTEM_BUSY(500002, "系统繁忙，请稍后再试"),
    DATABASE_ERROR(500003, "数据库异常"),
    REMOTE_SERVICE_ERROR(500004, "远程服务调用异常"),
    
    // 新增的失败状态
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败");
    
    // 状态码
    private final int code;
    
    // 消息
    private final String message;
    
    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
} 