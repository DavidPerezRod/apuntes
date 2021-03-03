package com.zerti.utilities.exception.hierarchy.checked.rest;

import com.zerti.utilities.exception.category.Micro;
import com.zerti.utilities.exception.hierarchy.checked.ZertiCheckedException;
import com.zerti.utilities.exception.ZertiExceptionBody;
import com.zerti.utilities.exception.hierarchy.rest.ZertiRestException;
import com.zerti.utilities.exception.hierarchy.rest.ZertiRestExceptionBody;
import org.springframework.http.HttpStatus;

public class ZertiRestCheckedException extends ZertiCheckedException implements ZertiRestException {

    protected ZertiRestCheckedException(ZertiExceptionBody body) {
        super(body);
    }

    protected ZertiRestCheckedException(ZertiExceptionBody body, Throwable cause) {
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
