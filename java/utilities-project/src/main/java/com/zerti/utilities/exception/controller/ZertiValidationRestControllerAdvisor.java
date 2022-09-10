package com.zerti.utilities.exception.controller;

import com.zerti.utilities.exception.ExceptionInfo;
import com.zerti.utilities.message.ZertiMessageResolver;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

import static com.zerti.utilities.message.ZertiLocaleResolver.getLocaleFromWebRequest;
import static com.zerti.utilities.message.MessageCodes.REST_CONTROLLER_PARAM_PARSING_ERROR;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@ControllerAdvice(annotations = RestController.class)
@Order(1) //the first controller applied
public class ZertiValidationRestControllerAdvisor extends ResponseEntityExceptionHandler {

	private final ZertiMessageResolver zertiMessageResolver;

	@Autowired
	ZertiValidationRestControllerAdvisor(ZertiMessageResolver zertiMessageResolver){
		this.zertiMessageResolver=zertiMessageResolver;
	}

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> fields = ex.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        return handleExceptionInternal(ex, fields, status);
    }

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        String cause = zertiMessageResolver.getMessage(REST_CONTROLLER_PARAM_PARSING_ERROR, getLocaleFromWebRequest(request));
        return new ResponseEntity<>(cause, BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Validation Error:", ex);
        return new ResponseEntity<>(ex.getMessage(), status);
    }

    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, List<String> fields, HttpStatus status) {
        log.error("Validation Error:", ex);
        return new ResponseEntity<>(fields, status);
    }

}


