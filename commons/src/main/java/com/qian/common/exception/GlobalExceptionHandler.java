package com.qian.common.exception;

import com.qian.common.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(ServiceException.class)
    public Response<Void> handleServiceException(ServiceException e, HttpServletRequest request) {
        log.error("请求地址'{}',发生业务异常.", request.getRequestURI(), e);
        Integer code = e.getCode();
        return code != null ? Response.error(code, e.getMessage()) : Response.error(e.getMessage());
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("请求地址'{}',发生参数校验异常.", request.getRequestURI(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return Response.error(message);
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    public Response<Void> handleBindException(BindException e, HttpServletRequest request) {
        log.error("请求地址'{}',发生参数绑定异常.", request.getRequestURI(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return Response.error(message);
    }

    /**
     * 处理请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response<Void> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.error("请求地址'{}',不支持'{}'请求", request.getRequestURI(), e.getMethod());
        return Response.error("不支持' " + e.getMethod() + "'请求");
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public Response<Void> handleException(Exception e, HttpServletRequest request) {
        log.error("请求地址'{}',发生未知异常.", request.getRequestURI(), e);
        return Response.error(e.getMessage());
    }
} 