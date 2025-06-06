package com.qian.common.log.aspect;

import com.qian.common.core.utils.ServletUtils;
import com.qian.common.core.utils.StringUtils;
import com.qian.common.log.annotation.Log;
import com.qian.common.log.domain.SysOperLog;
import com.qian.common.log.event.OperationLogEvent;
import com.qian.common.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 操作日志记录处理
 */
@Slf4j
@Aspect
@Component
public class LogAspect {
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     * @param result 返回值
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object result) {
        handleLog(joinPoint, controllerLog, null, result);
    }

    /**
     * 拦截异常操作
     * 
     * @param joinPoint 切点
     * @param e 异常
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) {
        try {
            // 获取当前的用户
            String username = SecurityUtils.getUsername();

            // 获取请求信息
            HttpServletRequest request = ServletUtils.getRequest();
            String ip = ServletUtils.getClientIP(request);
            String location = ServletUtils.getLocation(request);

            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            String method = className + "." + methodName + "()";

            // 设置请求方式
            String requestMethod = request.getMethod();

            // 设置请求参数
            String operationParam = "";
            if (controllerLog.isSaveRequestData()) {
                Map<String, String[]> params = request.getParameterMap();
                operationParam = StringUtils.mapToString(params);
            }

            // 设置返回参数
            String jsonResultStr = "";
            if (controllerLog.isSaveResponseData() && jsonResult != null) {
                jsonResultStr = StringUtils.toJsonString(jsonResult);
            }

            // 发布事件
            eventPublisher.publishEvent(new OperationLogEvent(
                this,
                controllerLog.title(),
                controllerLog.businessType(),
                method,
                requestMethod,
                1, // 操作类别：后台用户
                username,
                ip,
                location,
                operationParam,
                jsonResultStr,
                e != null ? 1 : 0, // 操作状态：0正常 1异常
                e != null ? e.getMessage() : null
            ));
        } catch (Exception exp) {
            log.error("记录操作日志异常", exp);
        }
    }
} 