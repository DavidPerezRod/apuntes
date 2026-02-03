package com.zerti.utilities.exception.hierarchy.rest.builder;

import com.zerti.utilities.exception.category.ExceptionItem;
import com.zerti.utilities.exception.category.Micro;
import com.zerti.utilities.exception.hierarchy.checked.rest.ZertiClientCheckedException;
import com.zerti.utilities.exception.hierarchy.rest.ZertiRestExceptionBody;
import com.zerti.utilities.exception.hierarchy.rest.type.HttpClientTypeError;
import com.zerti.utilities.exception.hierarchy.unchecked.rest.ZertiClientUncheckedException;

public class ZertiRestClientExceptionBuilder {

    private final ZertiRestExceptionBody body;
    private Throwable cause;

    private ZertiRestClientExceptionBuilder(ZertiRestExceptionBody body, Throwable cause) {
        this.cause=cause;
        this.body = body;
    }

    private ZertiRestClientExceptionBuilder(ZertiRestExceptionBody body) {
        this.body = body;
    }

    public static ZertiRestClientExceptionBuilder clientException(Micro micro, HttpClientTypeError status, String message, Throwable cause, ExceptionItem item) {
        return new ZertiRestClientExceptionBuilder(
                ZertiRestExceptionBody.builder().status(status.getStatus()).micro(micro).message(message).item(item).build(),cause);
    }


    public static ZertiRestClientExceptionBuilder clientException(Micro micro, HttpClientTypeError status, String message, Throwable cause) {
        return new ZertiRestClientExceptionBuilder(
                ZertiRestExceptionBody.builder().status(status.getStatus()).micro(micro).message(message).build(),cause);
    }

    public static ZertiRestClientExceptionBuilder clientException(Micro micro, HttpClientTypeError status, String message) {
        return new ZertiRestClientExceptionBuilder(
                ZertiRestExceptionBody.builder().status(status.getStatus()).micro(micro).message(message).build());
    }

    public RuntimeException unchecked() {
        if (cause!=null)
            return new ZertiClientUncheckedException(body, cause);
        else
            return new ZertiClientUncheckedException(body);
    }

    public Exception checked() {
        if (cause!=null)
            return new ZertiClientCheckedException(body, cause);
        else
            return new ZertiClientCheckedException(body);
    }


}
