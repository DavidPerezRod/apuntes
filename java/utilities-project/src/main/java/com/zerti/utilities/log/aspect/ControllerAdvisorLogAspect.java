package com.zerti.utilities.log.aspect;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.zerti.utilities.log.service.RepositoryLogService;
import com.zerti.utilities.message.ZertiMessageResolver;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.beans.Transient;

@Aspect
@Component
public class ControllerAdvisorLogAspect extends LogAspect {

    @Autowired
    public ControllerAdvisorLogAspect(
		RepositoryLogService logService,
		@Qualifier("defaultLogInfoCollector") LogInfoCollector logInfoCollector,
		ZertiMessageResolver zertiMessageResolver
	) {
        super(logInfoCollector, logService, zertiMessageResolver);
    }

    @Pointcut("execution(* com.loycuspay.psd2.*.exception.*.*(..))")
    public void loycupsPayControllerAdvisor() {}

    @Pointcut("execution(* com.zerti.utilities.exception.controller.*.*(..))")
    public void zertiUtilitiesControllerAdvisor() {}

    @Pointcut("loycupsPayControllerAdvisor() || zertiUtilitiesControllerAdvisor()")
    public void isControllerAdvisorPublicMethod() {}

    @Around("isControllerAdvisorPublicMethod()")
    public Object registerException(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return execute(proceedingJoinPoint);
    }
}
