package com.zerti.utilities.exception.hierarchy.unchecked;


import com.zerti.utilities.exception.ZertiException;
import com.zerti.utilities.exception.ZertiExceptionBody;

public class ZertiUncheckedException extends RuntimeException implements ZertiException {

    protected final ZertiExceptionBody body;

    public ZertiUncheckedException(final ZertiExceptionBody body) {
        this.body = body;
    }

    public ZertiUncheckedException(final ZertiExceptionBody body, Throwable cause) {
        super(cause);
        this.body = body;
    }

    @Override
    public String getMessage() {
        return body.getMessage();
    }
}
