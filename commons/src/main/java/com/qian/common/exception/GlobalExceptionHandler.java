package com.qian.common.exception;

import com.qian.common.core.domain.Response;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(ServiceException.class)
    public Response<String> handleServiceException(ServiceException e) {
        log.error(e.getMessage(), e);
        return Response.fail(e.getCode(), e.getMessage()).convert();
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(ConfigException.class)
    public Response<String> handleConfigException(ConfigException e) {
        log.error(e.getMessage(), e);
        return Response.fail(e.getCode(), e.getMessage()).convert();
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return Response.fail(message);
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    public Response<String> handleBindException(BindException e) {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return Response.fail(message);
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Response<String> handleConstraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        return Response.fail(message);
    }

    /**
     * 处理权限校验异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Response<String> handleAccessDeniedException(AccessDeniedException e) {
        log.error("没有权限，{}", e.getMessage());
        return Response.fail(403, "没有权限，不能访问").convert();
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public Response<String> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return Response.fail("服务器错误，请联系管理员");
    }
} 