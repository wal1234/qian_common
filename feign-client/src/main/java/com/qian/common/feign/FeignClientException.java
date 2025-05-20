package com.qian.common.feign;

import org.springframework.http.HttpStatus;

/**
 * 表示客户端错误的Feign异常（4xx状态码）
 */
public class FeignClientException extends FeignException {
    
    public FeignClientException(HttpStatus status, String methodKey) {
        super(status, methodKey);
    }
    
    @Override
    public String getMessage() {
        return String.format("Feign客户端错误 - 状态码: %d, 方法: %s", getStatus().value(), getMethodKey());
    }
} 