package com.qian.common.enums.common;

import lombok.Getter;

/**
 * 统一返回状态码枚举
 */
@Getter
public enum ResultCodeEnum {
    SUCCESS("000000", "操作成功"),
    SYSTEM_ERROR("999999", "系统异常"),
    PARAM_ERROR("100001", "参数校验失败"),
    DATA_NOT_FOUND("200001", "数据不存在");

    private final String code;
    private final String msg;

    ResultCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}