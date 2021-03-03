package com.zerti.utilities.exception.hierarchy.rest.builder;

import com.zerti.utilities.exception.category.ExceptionItem;
import com.zerti.utilities.exception.category.Micro;
import com.zerti.utilities.exception.hierarchy.checked.rest.ZertiRedirectionCheckedException;
import com.zerti.utilities.exception.hierarchy.rest.ZertiRestExceptionBody;
import com.zerti.utilities.exception.hierarchy.rest.type.HttpRedirectionTypeError;
import com.zerti.utilities.exception.hierarchy.unchecked.rest.ZertiRedirectionUncheckedException;

public class ZertiRestRedirectionExceptionBuilder {

    private final ZertiRestExceptionBody body;
    private Throwable cause;

    private ZertiRestRedirectionExceptionBuilder(ZertiRestExceptionBody body, Throwable cause) {
        this.cause = cause;
        this.body = body;
    }

    private ZertiRestRedirectionExceptionBuilder(ZertiRestExceptionBody body) {
        this.body = body;
    }

    public static ZertiRestRedirectionExceptionBuilder RedirectionException(Micro micro, HttpRedirectionTypeError status, String message, Throwable cause, ExceptionItem item) {
        return new ZertiRestRedirectionExceptionBuilder(
                ZertiRestExceptionBody.builder().status(status.getStatus()).micro(micro).message(message).item(item).build(), cause);
    }

    public static ZertiRestRedirectionExceptionBuilder RedirectionException(Micro micro, HttpRedirectionTypeError status, String message, Throwable cause) {
        return new ZertiRestRedirectionExceptionBuilder(
                ZertiRestExceptionBody.builder().status(status.getStatus()).micro(micro).message(message).build(), cause);
    }

    public static ZertiRestRedirectionExceptionBuilder RedirectionException(Micro micro, HttpRedirectionTypeError status, String message) {
        return new ZertiRestRedirectionExceptionBuilder(
                ZertiRestExceptionBody.builder().status(status.getStatus()).micro(micro).message(message).build());
    }

    public RuntimeException unchecked() {
        if (cause != null)
            return new ZertiRedirectionUncheckedException(body, cause);
        else
            return new ZertiRedirectionUncheckedException(body);
    }

    public Exception checked() {
        if (cause != null)
            return new ZertiRedirectionCheckedException(body, cause);
        else
            return new ZertiRedirectionCheckedException(body);
    }
}
