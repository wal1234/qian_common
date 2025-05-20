package com.qian.common.request;

import jakarta.validation.constraints.NotNull;

/**
 * 基础请求类
 */
public abstract class BaseRequest {
    
    /**
     * 请求ID
     */
    @NotNull(message = "请求ID不能为空")
    private String requestId;
    
    /**
     * 请求时间戳
     */
    @NotNull(message = "请求时间戳不能为空")
    private Long timestamp;
    
    /**
     * 参数校验方法
     * @return 校验结果
     */
    public abstract boolean validate();
    
    // Getters and Setters
    public String getRequestId() {
        return requestId;
    }
    
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    
    public Long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}