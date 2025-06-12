package com.qian.common.response;

import java.io.Serializable;
import com.qian.common.enums.common.ErrorCodeEnum;
import com.qian.common.constants.ResultCode;
import lombok.Data;

/**
 * 通用响应对象
 */
@Data
public class Response<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 状态码 */
    private Integer code;

    /** 返回内容 */
    private String message;

    /** 数据对象 */
    private T data;

    public Response() {
    }

    /**
     * 初始化一个新创建的 Response 对象
     *
     * @param code 状态码
     * @param msg 返回内容
     */
    public Response(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 初始化一个新创建的 Response 对象
     *
     * @param code 状态码
     * @param msg 返回内容
     * @param data 数据对象
     */
    public Response(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 返回成功消息（无数据）
     */
    public static <T> Response<T> success() {
        return new Response<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    /**
     * 返回成功消息（带数据）
     */
    public static <T> Response<T> success(T data) {
        return new Response<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static <T> Response<T> success(String message, T data) {
        return new Response<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 返回失败消息（自定义内容）
     */
    public static <T> Response<T> error(String msg) {
        return new Response<>(ResultCode.FAILED.getCode(), msg, null);
    }

    /**
     * 返回失败消息
     */
    public static <T> Response<T> error() {
        return new Response<>(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMessage(), null);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg 返回内容
     * @return 警告消息
     */
    public static <T> Response<T> error(Integer code, String message) {
        return new Response<>(code, message, null);
    }

    public static <T> Response<T> error(ResultCode resultCode) {
        return new Response<>(resultCode.getCode(), resultCode.getMessage(), null);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> Response<T> error(ErrorCodeEnum errorCode) {
        return new Response<>(errorCode.getCode(), errorCode.getMessage(), null);
    }
} 