package com.qian.common.feign;

import org.springframework.http.HttpStatus;

public class FeignException extends RuntimeException {
    
    private final HttpStatus status;
    private final String methodKey;
    
    public FeignException(HttpStatus status, String methodKey) {
        super(String.format("Feign调用异常 - 状态码: %d, 方法: %s", status.value(), methodKey));
        this.status = status;
        this.methodKey = methodKey;
    }
    
    public HttpStatus getStatus() {
        return status;
    }
    
    public String getMethodKey() {
        return methodKey;
    }
}