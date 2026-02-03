package com.zerti.utilities.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class DefaultMessageResolver{

    private final Hashtable<Object, ExposedResourceMessageBundleSource> messages = new Hashtable<>();

    @Autowired
    public DefaultMessageResolver(@Qualifier("utilities-messages") MessageSource utilitiesMessageSource, List<ExposedResourceMessageBundleSource> messageSources) {
        log.info("Message properties resource: {}", utilitiesMessageSource.toString());
        joinMessageResources(messageSources);
    }

    public String getMessage(String var1, Object[] var2, Locale var3) throws NoSuchMessageException {
        return getMessageSource(var1).getMessage(var1, var2, var3);
    }

    public String getMessage(String var1, Locale var3) throws NoSuchMessageException {
        return getMessageSource(var1).getMessage(var1, null, var3);
    }

    private MessageSource getMessageSource(String messageCode) {
        return messages.get(messageCode);
    }

    private void joinMessageResources(List<ExposedResourceMessageBundleSource> messageSources) {
        for (ExposedResourceMessageBundleSource resource : messageSources) {
            Properties properties = resource.getMessages(LocaleContextHolder.getLocale());
            Set<Object> keys = properties.keySet();
            for (Object key : keys) {
                if (!messages.containsKey(key)) {
                    messages.put(key, resource);
                }
            }
        }
    }
}

