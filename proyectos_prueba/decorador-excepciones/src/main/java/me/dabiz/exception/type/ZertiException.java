package me.dabiz.exception.type;

import me.dabiz.exception.info.ExceptionInfo;

import java.io.PrintStream;

public interface ZertiException{
    String getMessage();
    Throwable getCause();
    void printStackTrace();
    void printStackTrace(PrintStream s);
    StackTraceElement[] getStackTrace();
    ExceptionInfo getExceptionInfo();
}
