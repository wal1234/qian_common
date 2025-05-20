package com.qian.common.feign;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign配置类 
 * 
 * 注意：此类为简化配置，与com.qian.common.feign.config.FeignConfiguration有重复定义
 * 推荐使用FeignConfiguration进行完整配置
 */
@Configuration
public class FeignConfig {
    
    /**
     * 已在FeignConfiguration中定义相同功能，此处保留兼容性
     * @deprecated 使用 com.qian.common.feign.config.FeignConfiguration代替
     */
    @Bean
    @Deprecated
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }
}