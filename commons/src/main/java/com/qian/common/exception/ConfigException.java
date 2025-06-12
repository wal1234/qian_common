package com.qian.common.exception;

/**
 * 参数配置异常
 */
public class ConfigException extends BaseException {
    private static final long serialVersionUID = 1L;

    public ConfigException(String message) {
        super(null, null, message);
    }

    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }
} 