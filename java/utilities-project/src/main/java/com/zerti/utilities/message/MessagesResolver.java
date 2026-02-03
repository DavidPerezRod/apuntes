package com.zerti.utilities.message;

import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

public interface MessagesResolver {

    String getMessage(String var1, Object[] var2, Locale var3);

    String getMessage(String var1, Locale var2) throws NoSuchMessageException;
}
