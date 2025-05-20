package com.qian.common.feign.fallback;

import com.qian.common.enums.common.ErrorCodeEnum;
import com.qian.common.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * 默认Feign降级工厂
 */
@Slf4j
public class DefaultFallbackFactory<T> implements FallbackFactory<T> {

    private final Class<T> targetType;

    public DefaultFallbackFactory(Class<T> targetType) {
        this.targetType = targetType;
    }

    @Override
    public T create(Throwable cause) {
        log.error("服务调用异常，接口类型: {}, 异常信息: {}", targetType.getName(), cause.getMessage(), cause);
        
        return FallbackProxyCreator.create(targetType, method -> {
            log.error("执行服务降级, 接口: {}, 方法: {}", targetType.getName(), method.getName());
            Class<?> returnType = method.getReturnType();
            
            // 如果返回类型是Response，则返回通用的错误响应
            if (Response.class.isAssignableFrom(returnType)) {
                return Response.error(ErrorCodeEnum.PARAM_ERROR);
            }
            
            // 对于其他类型的返回值，根据类型返回默认值
            if (returnType.isPrimitive()) {
                if (returnType == boolean.class) {
                    return false;
                } else if (returnType == char.class) {
                    return '\u0000';
                } else {
                    return 0;
                }
            }
            
            return null;
        });
    }
} 