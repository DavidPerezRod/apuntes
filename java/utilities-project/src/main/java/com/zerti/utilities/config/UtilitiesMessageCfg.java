package com.zerti.utilities.config;

import com.zerti.utilities.message.ExposedResourceMessageBundleSource;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.zerti.utilities.message"})
public class UtilitiesMessageCfg {
    @Bean(name="utilities-messages")
    public MessageSource messageSource() {
        ExposedResourceMessageBundleSource messageSource = new ExposedResourceMessageBundleSource();
        messageSource.setBasename("classpath:utilities-messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
