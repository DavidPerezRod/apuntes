package com.zerti.utilities.exception.hierarchy.checked.rest;

import com.zerti.utilities.exception.hierarchy.rest.ZertiRestExceptionBody;

public class ZertiClientCheckedException extends ZertiRestCheckedException{

    public ZertiClientCheckedException(ZertiRestExceptionBody body) {
        super(body);
    }

    public ZertiClientCheckedException(ZertiRestExceptionBody body, Throwable cause) {
        super(body, cause);
    }
}
