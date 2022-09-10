package com.zerti.utilities.exception.hierarchy.unchecked.rest;

import com.zerti.utilities.exception.hierarchy.rest.ZertiRestExceptionBody;
import com.zerti.utilities.exception.hierarchy.unchecked.rest.ZertiRestUncheckedException;

public class ZertiClientUncheckedException extends ZertiRestUncheckedException{

    public ZertiClientUncheckedException(ZertiRestExceptionBody body) {
        super(body);
    }

    public ZertiClientUncheckedException(ZertiRestExceptionBody body, Throwable cause) {
        super(body, cause);
    }
}
