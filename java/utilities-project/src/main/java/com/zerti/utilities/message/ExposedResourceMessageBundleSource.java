package com.zerti.utilities.message;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

public class ExposedResourceMessageBundleSource  extends ReloadableResourceBundleMessageSource {

    protected Properties loadProperties(Resource resource, String fileName) throws IOException {
        return super.loadProperties(resource, fileName);
    }

    Properties getMessages(Locale locale){
        return getMergedProperties(locale).getProperties();
    }
}
