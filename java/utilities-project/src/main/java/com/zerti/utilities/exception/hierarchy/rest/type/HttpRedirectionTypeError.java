package com.zerti.utilities.exception.hierarchy.rest.type;

import org.springframework.http.HttpStatus;

public enum HttpRedirectionTypeError {

    //no se han incluido los errores de cliente deprecados org.springframework.http.HttpStatus
    MULTIPLE_CHOICES(HttpStatus.MULTIPLE_CHOICES),
    MOVED_PERMANENTLY(HttpStatus.MOVED_PERMANENTLY),
    FOUND(HttpStatus.FOUND),
    SEE_OTHER(HttpStatus.SEE_OTHER),
    NOT_MODIFIED(HttpStatus.NOT_MODIFIED),
    TEMPORARY_REDIRECT(HttpStatus.TEMPORARY_REDIRECT),
    PERMANENT_REDIRECT(HttpStatus.PERMANENT_REDIRECT);

    private final HttpStatus status;

    HttpRedirectionTypeError(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus getStatus(){
        return status;
    }

}


