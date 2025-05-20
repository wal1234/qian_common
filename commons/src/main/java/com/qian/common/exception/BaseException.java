package com.qian.common.exception;

import com.qian.common.enums.common.ErrorCodeEnum;

public class BaseException extends RuntimeException {
    private int code;
    private String message;
    
    public BaseException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    
    public BaseException(ErrorCodeEnum errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
    
    public int getCode() {
        return code;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
    
    public void handleScenario() {
        // 这里可以添加场景处理逻辑
        // 例如记录日志、发送通知等
    }
}