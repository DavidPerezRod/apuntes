package com.zerti.utilities.exception.hierarchy.unchecked.log;

import com.zerti.utilities.exception.ZertiException;
import com.zerti.utilities.exception.ZertiExceptionBody;
import com.zerti.utilities.exception.hierarchy.unchecked.ZertiUncheckedException;

public class ZertiLogAspectException extends ZertiUncheckedException implements ZertiException {

    public ZertiLogAspectException(ZertiExceptionBody body, Throwable cause) {
        super(body, cause);
    }
}

