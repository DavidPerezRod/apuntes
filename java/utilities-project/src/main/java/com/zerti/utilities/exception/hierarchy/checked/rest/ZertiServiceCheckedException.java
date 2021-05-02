package com.zerti.utilities.exception.hierarchy.checked.rest;

import com.zerti.utilities.exception.hierarchy.rest.ZertiRestExceptionBody;
import com.zerti.utilities.exception.hierarchy.checked.rest.ZertiRestCheckedException;

public class ZertiServiceCheckedException extends ZertiRestCheckedException{

    public ZertiServiceCheckedException(ZertiRestExceptionBody body) {
        super(body);
    }

    public ZertiServiceCheckedException(ZertiRestExceptionBody body, Throwable cause) {
        super(body, cause);
    }
}
