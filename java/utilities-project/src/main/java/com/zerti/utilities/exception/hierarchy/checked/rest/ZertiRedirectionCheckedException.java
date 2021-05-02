package com.zerti.utilities.exception.hierarchy.checked.rest;

import com.zerti.utilities.exception.hierarchy.rest.ZertiRestExceptionBody;

public class ZertiRedirectionCheckedException extends ZertiRestCheckedException{

    public ZertiRedirectionCheckedException(ZertiRestExceptionBody body) {
        super(body);
    }
    public ZertiRedirectionCheckedException(ZertiRestExceptionBody body, Throwable cause) {
        super(body, cause);
    }
}
