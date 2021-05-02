package com.zerti.utilities.exception;

import java.io.PrintStream;

public interface ZertiException{
    String getMessage();
    Throwable getCause();
    void printStackTrace();
    void printStackTrace(PrintStream s);
    StackTraceElement[] getStackTrace();
}
