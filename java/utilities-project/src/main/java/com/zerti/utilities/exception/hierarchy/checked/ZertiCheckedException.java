package com.zerti.utilities.exception.hierarchy.checked;

import com.zerti.utilities.exception.ZertiException;
import com.zerti.utilities.exception.ZertiExceptionBody;

public class ZertiCheckedException extends Exception implements ZertiException {

    public final ZertiExceptionBody body;

    public ZertiCheckedException(ZertiExceptionBody body) {
        this.body = body;
    }

    public ZertiCheckedException(ZertiExceptionBody body, Throwable cause) {
        super(cause);
        this.body = body;
    }

    @Override
    public String getMessage() {
        return body.getMessage();
    }
}
