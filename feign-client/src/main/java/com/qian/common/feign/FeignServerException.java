package com.qian.common.feign;

import org.springframework.http.HttpStatus;

/**
 * 表示远程服务器错误的Feign异常（5xx状态码）
 */
public class FeignServerException extends FeignException {
    
    public FeignServerException(HttpStatus status, String methodKey) {
        super(status, methodKey);
    }
    
    @Override
    public String getMessage() {
        return String.format("Feign服务端错误 - 状态码: %d, 方法: %s", getStatus().value(), getMethodKey());
    }
} 