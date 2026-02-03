package com.zerti.utilities.exception.controller;

import com.zerti.utilities.exception.ExceptionInfo;
import com.zerti.utilities.exception.hierarchy.checked.ZertiCheckedException;
import com.zerti.utilities.exception.ZertiException;
import com.zerti.utilities.exception.hierarchy.unchecked.ZertiUncheckedException;
import com.zerti.utilities.exception.hierarchy.checked.rest.ZertiRestCheckedException;
import com.zerti.utilities.exception.hierarchy.rest.ZertiRestException;
import com.zerti.utilities.exception.hierarchy.unchecked.rest.ZertiRestUncheckedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@ControllerAdvice(annotations = RestController.class)
@Order() //the last controller applied
public class ZertiDefaultRestControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ZertiRestCheckedException.class, ZertiRestUncheckedException.class})
    @ResponseBody
    public ResponseEntity<String> handle(final ZertiRestException ex) {
        return handleExceptionInternal(ex.getMessage(), ex.getStatus(), ex.getCause());
    }

    @ExceptionHandler({ZertiUncheckedException.class, ZertiCheckedException.class})
    @ResponseBody
    public ResponseEntity<String> handle(final ZertiException ex) {
        return handleExceptionInternal(ex.getMessage(), INTERNAL_SERVER_ERROR, ex.getCause());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ResponseEntity<String> handle(Throwable cause) {
        return handleExceptionInternal(cause.getMessage(), INTERNAL_SERVER_ERROR, cause);
    }

    protected ResponseEntity<String> handleExceptionInternal(String businessMessage, HttpStatus status, Throwable cause) {
        log.error(businessMessage,cause);
        return new ResponseEntity<>(businessMessage, status);
    }
}


