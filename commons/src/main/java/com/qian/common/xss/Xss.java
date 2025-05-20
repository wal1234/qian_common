package com.qian.common.xss;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 防止XSS攻击注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
public @interface Xss {
    /**
     * 错误消息
     */
    String message() default "不允许任何脚本运行";

    /**
     * 验证组
     */
    Class<?>[] groups() default {};
} 