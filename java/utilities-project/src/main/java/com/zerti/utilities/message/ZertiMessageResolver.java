package com.zerti.utilities.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class ZertiMessageResolver {

    private final DefaultMessageResolver messageResolver;

    @Autowired
    ZertiMessageResolver(final DefaultMessageResolver messageResolver){
         this.messageResolver = messageResolver;
    }

    public String getMessage(ZertiMessageCode messageCode, Object[] var1, Locale var2) {
        return messageResolver.getMessage(messageCode.getMessageCode(), var1, var2);
    }

    public String getMessage(ZertiMessageCode messageCode, Locale var1) throws NoSuchMessageException {
        return messageResolver.getMessage(messageCode.getMessageCode(),var1);
    }
}
