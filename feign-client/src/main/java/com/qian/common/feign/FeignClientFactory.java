package com.qian.common.feign;

import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;

@Import(FeignClientsConfiguration.class)
public class FeignClientFactory {
    
    public static <T> T createClient(Class<T> apiType, String url) {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logger(new Slf4jLogger(apiType))
                .logLevel(Logger.Level.FULL)
                .options(new Request.Options(5000, 10000))
                .retryer(new Retryer.Default(100, 1000, 3))
                .target(apiType, url);
    }
}