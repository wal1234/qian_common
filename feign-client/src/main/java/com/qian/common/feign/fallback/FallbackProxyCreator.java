package com.qian.common.feign.fallback;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.Function;

/**
 * 降级代理创建器
 */
public class FallbackProxyCreator {

    /**
     * 创建代理对象
     *
     * @param interfaceType 接口类型
     * @param fallbackFunction 降级处理函数
     * @param <T> 泛型
     * @return 代理对象
     */
    public static <T> T create(Class<T> interfaceType, Function<Method, Object> fallbackFunction) {
        InvocationHandler handler = (proxy, method, args) -> {
            // Object类的方法直接调用
            if (method.getDeclaringClass() == Object.class) {
                return method.invoke(proxy, args);
            }
            
            // 调用降级处理函数
            return fallbackFunction.apply(method);
        };
        
        // 创建接口代理
        return interfaceType.cast(Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class<?>[] { interfaceType },
                handler
        ));
    }
} 