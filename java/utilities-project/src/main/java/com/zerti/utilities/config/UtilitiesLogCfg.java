package com.zerti.utilities.config;

import com.zerti.utilities.log.repository.LogRepository;
import com.zerti.utilities.message.ExposedResourceMessageBundleSource;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackageClasses = LogRepository.class)
@ComponentScan(basePackages = {"com.zerti"})
@EnableAspectJAutoProxy
public class UtilitiesLogCfg {

//    @Bean(name="utilities-messages")
//    public MessageSource messageSource() {
//        ExposedResourceMessageBundleSource messageSource = new ExposedResourceMessageBundleSource();
//        messageSource.setBasename("classpath:utilities-messages");
//        messageSource.setDefaultEncoding("UTF-8");
//        return messageSource;
//    }
}
