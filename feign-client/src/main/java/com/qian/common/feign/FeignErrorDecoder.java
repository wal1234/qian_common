package com.qian.common.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Feign错误解码器，将HTTP错误转换为自定义异常
 */
@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {
    
    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus status = HttpStatus.valueOf(response.status());
        
        String body = null;
        try {
            if (response.body() != null) {
                body = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.warn("读取响应体失败", e);
        }
        
        log.debug("Feign请求异常 - 方法: {}, 状态码: {}, 响应体: {}", methodKey, status.value(), body);
        
        if (status.is5xxServerError()) {
            return new FeignServerException(status, methodKey);
        } else if (status.is4xxClientError()) {
            return new FeignClientException(status, methodKey);
        }
        
        return new FeignException(status, methodKey);
    }
}