package com.qian.common.response;

import com.qian.common.enums.common.ErrorCodeEnum;
import lombok.Data;

@Data
public class Response<T> {
    // Getter methods
    private Integer code;
    private String message;
    private T data;

    public Response() {
    }

    private Response(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(200, "操作成功", data);
    }

    /**
     * 成功，无数据
     */
    public static <T> Response<T> success() {
        return success(null);
    }

    public static <T> Response<T> error(String message) {
        return new Response<>(500, message, null);
    }

    public static <T> Response<T> error(Integer code, String message) {
        return new Response<>(code, message, null);
    }

    public static <T> Response<T> error(ErrorCodeEnum errorCode) {
        return new Response<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    // Setter methods
    public void setCode(int code) { this.code = code; }
    public void setMessage(String message) { this.message = message; }
    public void setData(T data) { this.data = data; }
} 