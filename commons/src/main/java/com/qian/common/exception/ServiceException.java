package com.qian.common.exception;

import com.qian.common.enums.common.ErrorCodeEnum;

/**
 * 业务异常
 */
public class ServiceException extends BaseException {
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 错误明细，内部调试错误
     */
    private String detailMessage;

    /**
     * 空构造方法，避免反序列化问题
     */
    public ServiceException() {
        super(500, "业务异常");
    }

    public ServiceException(String message) {
        super(500, message);
    }

    public ServiceException(String message, Integer code) {
        super(code, message);
    }

    public ServiceException(ErrorCodeEnum errorCode) {
        super(errorCode);
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getCode() {
        return code;
    }

    public ServiceException setMessage(String message) {
        this.message = message;
        return this;
    }

    public ServiceException setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }
} 