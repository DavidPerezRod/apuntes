package com.zerti.utilities.validation.url;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = UrlPatternValidator.class)
@Target({ ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.TYPE_PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUrl {

	String message() default "{utilities.validation.url.false}";
	
	boolean required() default true;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
