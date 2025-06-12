package com.qian.common.utils;

import com.qian.common.annotation.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;

/**
 * 日志工具类
 */
public class LogUtils {
    private static final Logger log = LoggerFactory.getLogger(LogUtils.class);

    /**
     * 获取用户
     */
    public static String getUsername() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            return request.getHeader("username");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取IP地址
     */
    public static String getIp() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            return IpUtils.getIpAddr(request);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取请求URI
     */
    public static String getRequestUri() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            return request.getRequestURI();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取请求方法
     */
    public static String getRequestMethod() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            return request.getMethod();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取请求参数
     */
    public static String getRequestParams() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            return request.getQueryString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取请求头
     */
    public static String getRequestHeaders() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            return Collections.list(request.getHeaderNames()).toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取请求体
     */
    public static String getRequestBody() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            return request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取响应体
     */
    public static String getResponseBody() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            return request.getAttribute("responseBody") != null ? request.getAttribute("responseBody").toString() : "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取异常信息
     */
    public static String getExceptionMessage(Exception e) {
        return e.getMessage();
    }

    /**
     * 获取异常堆栈
     */
    public static String getExceptionStackTrace(Exception e) {
        return Arrays.toString(e.getStackTrace());
    }

    /**
     * 获取业务类型
     */
    public static String getBusinessType(Log log) {
        return log.businessType();
    }

    /**
     * 获取模块
     */
    public static String getTitle(Log log) {
        return log.title();
    }

    /**
     * 获取是否保存请求的参数
     */
    public static boolean isSaveRequestData(Log log) {
        return log.isSaveRequestData();
    }

    /**
     * 获取是否保存响应的参数
     */
    public static boolean isSaveResponseData(Log log) {
        return log.isSaveResponseData();
    }
} 