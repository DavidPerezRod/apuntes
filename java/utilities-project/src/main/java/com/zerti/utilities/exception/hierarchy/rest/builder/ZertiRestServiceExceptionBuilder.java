package com.zerti.utilities.exception.hierarchy.rest.builder;

import com.zerti.utilities.exception.category.ExceptionItem;
import com.zerti.utilities.exception.category.Micro;
import com.zerti.utilities.exception.hierarchy.checked.rest.ZertiServiceCheckedException;
import com.zerti.utilities.exception.hierarchy.rest.ZertiRestExceptionBody;
import com.zerti.utilities.exception.hierarchy.rest.type.HttpServiceTypeError;
import com.zerti.utilities.exception.hierarchy.unchecked.rest.ZertiServiceUncheckedException;

public class ZertiRestServiceExceptionBuilder {

    private final ZertiRestExceptionBody body;
    private Throwable cause;

    private ZertiRestServiceExceptionBuilder(ZertiRestExceptionBody body, Throwable cause) {
        this.cause = cause;
        this.body = body;
    }

    private ZertiRestServiceExceptionBuilder(ZertiRestExceptionBody body) {
        this.body = body;
    }

    public static ZertiRestServiceExceptionBuilder serviceException(Micro micro, HttpServiceTypeError status, String message, Throwable cause, ExceptionItem item) {
        return new ZertiRestServiceExceptionBuilder(
                ZertiRestExceptionBody.builder().status(status.getStatus()).micro(micro).message(message).item(item).build(), cause);
    }

    public static ZertiRestServiceExceptionBuilder serviceException(Micro micro, HttpServiceTypeError status, String message, Throwable cause) {
        return new ZertiRestServiceExceptionBuilder(
                ZertiRestExceptionBody.builder().status(status.getStatus()).micro(micro).message(message).build(), cause);
    }

    public static ZertiRestServiceExceptionBuilder serviceException(Micro micro, HttpServiceTypeError status, String message) {
        return new ZertiRestServiceExceptionBuilder(
                ZertiRestExceptionBody.builder().status(status.getStatus()).micro(micro).message(message).build()
        );
    }

    public RuntimeException unchecked() {
        if (cause != null)
            return new ZertiServiceUncheckedException(body,cause);
        else
            return new ZertiServiceUncheckedException(body);
    }

    public Exception checked(){
        if (cause != null)
            return new ZertiServiceCheckedException(body,cause);
        else
            return new ZertiServiceCheckedException(body);
    }
}
