package com.peiwan.exception;

import com.peiwan.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

/**
 * 全局异常处理器
 * 
 * @author peiwan
 * @since 2024-01-01
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理业务异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("业务异常: {}", e.getMessage(), e);
        ApiResponse<Void> response = ApiResponse.<Void>error(400, e.getMessage())
                .requestId(request.getHeader("X-Request-Id"));
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("参数验证异常: {}", e.getMessage());
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        ApiResponse<Void> response = ApiResponse.<Void>error(400, message)
                .requestId(request.getHeader("X-Request-Id"));
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 处理绑定异常
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<Void>> handleBindException(BindException e, HttpServletRequest request) {
        log.error("绑定异常: {}", e.getMessage());
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        ApiResponse<Void> response = ApiResponse.<Void>error(400, message)
                .requestId(request.getHeader("X-Request-Id"));
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 处理约束违反异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        log.error("约束违反异常: {}", e.getMessage());
        ApiResponse<Void> response = ApiResponse.<Void>error(400, e.getMessage())
                .requestId(request.getHeader("X-Request-Id"));
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常: {}", e.getMessage(), e);
        ApiResponse<Void> response = ApiResponse.<Void>error(500, "系统内部错误")
                .requestId(request.getHeader("X-Request-Id"));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

