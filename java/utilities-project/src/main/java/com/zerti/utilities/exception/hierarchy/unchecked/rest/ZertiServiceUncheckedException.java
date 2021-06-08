package com.zerti.utilities.exception.hierarchy.unchecked.rest;

import com.zerti.utilities.exception.hierarchy.rest.ZertiRestExceptionBody;
import com.zerti.utilities.exception.hierarchy.unchecked.rest.ZertiRestUncheckedException;

public class ZertiServiceUncheckedException extends ZertiRestUncheckedException{

    public ZertiServiceUncheckedException(ZertiRestExceptionBody body) {
        super(body);
    }

    public ZertiServiceUncheckedException(ZertiRestExceptionBody body, Throwable cause) {
        super(body, cause);
    }
}
