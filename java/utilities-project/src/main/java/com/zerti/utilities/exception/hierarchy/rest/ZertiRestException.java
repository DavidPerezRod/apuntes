package com.zerti.utilities.exception.hierarchy.rest;

import com.zerti.utilities.exception.ZertiException;
import com.zerti.utilities.exception.category.Micro;
import org.springframework.http.HttpStatus;

public interface ZertiRestException extends ZertiException {

    HttpStatus getStatus();
    Micro getFlujo();
}
