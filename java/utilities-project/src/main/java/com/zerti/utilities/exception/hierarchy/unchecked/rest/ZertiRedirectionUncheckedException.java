package com.zerti.utilities.exception.hierarchy.unchecked.rest;

import com.zerti.utilities.exception.hierarchy.rest.ZertiRestExceptionBody;
import com.zerti.utilities.exception.hierarchy.unchecked.rest.ZertiRestUncheckedException;

public class ZertiRedirectionUncheckedException extends ZertiRestUncheckedException{

    public ZertiRedirectionUncheckedException(ZertiRestExceptionBody body) {
        super(body);
    }

    public ZertiRedirectionUncheckedException(ZertiRestExceptionBody body, Throwable cause) {
        super(body, cause);
    }
}
