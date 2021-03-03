package com.zerti.utilities.log.aspect;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Kpi {
    String service() default "";
    String flow() default "";
    String domain() default "";
    String step()  default "";
    String dependency() default "";
}
