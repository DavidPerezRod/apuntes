package com.zerti.utilities.exception.hierarchy.unchecked.rest;

import com.zerti.utilities.exception.category.Micro;
import com.zerti.utilities.exception.hierarchy.rest.ZertiRestException;
import com.zerti.utilities.exception.hierarchy.rest.ZertiRestExceptionBody;
import com.zerti.utilities.exception.hierarchy.unchecked.ZertiUncheckedException;
import org.springframework.http.HttpStatus;

public class ZertiRestUncheckedException extends ZertiUncheckedException implements ZertiRestException {

    protected ZertiRestUncheckedException(ZertiRestExceptionBody body) {
        super(body);
    }

    protected ZertiRestUncheckedException(ZertiRestExceptionBody body, Throwable cause) {
        super(body, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return ((ZertiRestExceptionBody)body).getStatus();
    }

    @Override
    public Micro getFlujo() {
        return ((ZertiRestExceptionBody)body).getMicro();
    }
}