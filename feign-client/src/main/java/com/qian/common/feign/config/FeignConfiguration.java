package com.qian.common.feign.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import com.qian.common.feign.FeignErrorDecoder;

/**
 * Feign配置类
 */
@Configuration
public class FeignConfiguration {

    /**
     * 日志级别
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * 表单编码器
     */
    @Bean
    public Encoder feignEncoder(ObjectFactory<HttpMessageConverters> converters) {
        return new SpringFormEncoder(new SpringEncoder(converters));
    }

    /**
     * 解码器
     */
    @Bean
    public Decoder feignDecoder(ObjectFactory<HttpMessageConverters> converters) {
        return new SpringDecoder(converters);
    }

    /**
     * 请求拦截器，传递请求头信息
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            // 设置Content-Type为JSON
            template.header("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            template.header("Accept", MediaType.APPLICATION_JSON_VALUE);

            // 获取当前请求的请求头信息并传递
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                
                // 需要向下传递的请求头
                List<String> headersToForward = new ArrayList<>();
                headersToForward.add("Authorization");
                headersToForward.add("X-Request-ID");
                headersToForward.add("X-Real-IP");
                headersToForward.add("X-Forwarded-For");
                
                // 传递请求头
                for (String header : headersToForward) {
                    String headerValue = request.getHeader(header);
                    if (headerValue != null && !headerValue.isEmpty()) {
                        template.header(header, headerValue);
                    }
                }
            }
        };
    }

    /**
     * 错误解码器
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }
} 