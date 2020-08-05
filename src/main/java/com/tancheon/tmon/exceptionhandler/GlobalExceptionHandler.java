package com.tancheon.tmon.exceptionhandler;

import com.tancheon.tmon.domain.response.GeneralResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

// TODO - 상세 처리를 위해 분류해놓음. 우선은 이대로 처리
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * '@ModelAttribute' 객체가 Binding 되지 못하는 경우 발생
     * (@Valid 객체 필드에 설정된 Validation 어노테이션 제약 조건을 충족하지 못하는 경우도 해당)
     */
    @ExceptionHandler(value = BindException.class)
    public ResponseEntity handleBindException(Exception e) {
        return toResponseEntity(e);
    }

    /**
     * '@Validated'로 설정된 클래스에 '@RequestParam' 필드에 설정된 Validation 어노테이션 제약 조건을 충족하지 못하는 경우 발생
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity handleConstraintViolationException(Exception e) {
        return toResponseEntity(e);
    }

    /**
     * required = true, @NotNull, @NotEmpty 인자가 누락되었을 경우 발생
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity handleMissingServletRequestParameterException(Exception e) {
        return toResponseEntity(e);
    }

    /**
     * 지원하지 않는 HTTP method 호출 시 발생
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity handleHttpRequestMethodNotSupportedException(Exception e) {
        return toResponseEntity(e);
    }

    /**
     * Request Parameter 타입이 일치하지 않을 경우 발생
     */
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity handleMethodArgumentTypeMismatchException(Exception e) {
        return toResponseEntity(e);
    }

    /**
     * '@RequestBody, @RequestPart' 어노테이션으로 받은 인자가 binding에 실패할 경우 발생
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(Exception e) {
        return toResponseEntity(e);
    }

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity handleAccessDeniedException(Exception e) {
        return toResponseEntity(e);
    }

    /**
     * 이외에 발생하는 모든 Exception 처리
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity handleException(Exception e) {
        return toResponseEntity(e);
    }

    private ResponseEntity toResponseEntity(Exception e) {
        GeneralResponse response = GeneralResponse.INTERNAL_SERVER_ERROR;
        response.setCause(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }
}
