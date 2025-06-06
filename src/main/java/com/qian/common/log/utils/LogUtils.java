package com.qian.common.log.utils;

import com.qian.common.core.utils.ServletUtils;
import com.qian.common.log.event.LoginLogEvent;
import com.qian.common.log.event.OperationLogEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 日志工具类
 */
@Slf4j
@Component
public class LogUtils {
    private static ApplicationEventPublisher eventPublisher;

    public LogUtils(ApplicationEventPublisher eventPublisher) {
        LogUtils.eventPublisher = eventPublisher;
    }

    /**
     * 记录登录日志
     *
     * @param username 用户名
     * @param status 状态
     * @param msg 消息
     */
    public static void recordLoginLog(String username, String status, String msg) {
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            String ip = ServletUtils.getClientIP(request);
            String location = ServletUtils.getLocation(request);
            String browser = ServletUtils.getBrowser(request);
            String os = ServletUtils.getOs(request);

            eventPublisher.publishEvent(new LoginLogEvent(
                LogUtils.class,
                username,
                ip,
                location,
                browser,
                os,
                status,
                msg
            ));
        } catch (Exception e) {
            log.error("记录登录日志异常", e);
        }
    }

    /**
     * 记录操作日志
     *
     * @param title 模块名称
     * @param businessType 业务类型
     * @param method 方法名称
     * @param requestMethod 请求方式
     * @param operatorType 操作类别
     * @param operationName 操作人员
     * @param operationIp 操作IP
     * @param operationLocation 操作地点
     * @param operationParam 操作参数
     * @param jsonResult 返回结果
     * @param status 操作状态
     * @param errorMsg 错误消息
     */
    public static void recordOperLog(String title, Integer businessType, String method,
                                   String requestMethod, Integer operatorType, String operationName,
                                   String operationIp, String operationLocation, String operationParam,
                                   String jsonResult, Integer status, String errorMsg) {
        try {
            eventPublisher.publishEvent(new OperationLogEvent(
                LogUtils.class,
                title,
                businessType,
                method,
                requestMethod,
                operatorType,
                operationName,
                operationIp,
                operationLocation,
                operationParam,
                jsonResult,
                status,
                errorMsg
            ));
        } catch (Exception e) {
            log.error("记录操作日志异常", e);
        }
    }
} 