package com.zerti.utilities.log.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.zerti.utilities.message.ZertiMessageResolver;

@Aspect
@Component
@Slf4j
public class GlobalAspect extends LogAspect {

    @Autowired
    GlobalAspect(
		@Qualifier("defaultLogInfoCollector") LogInfoCollector logInfoCollector,
		ZertiMessageResolver zertiMessageResolver
	){
        super(logInfoCollector, zertiMessageResolver);
    }

    @Pointcut("@within(com.zerti.utilities.log.aspect.Global) && !@annotation(com.zerti.utilities.log.aspect.Kpi)")
	public void beanAnnotatedWithMonitor() {}

	@Pointcut("execution(* *(..))")
	public void publicMethod() {}

	@Pointcut("publicMethod() && beanAnnotatedWithMonitor()")
	public void publicMethodInsideAClassMarkedWithAtMonitor() {}
	
    @Around("publicMethodInsideAClassMarkedWithAtMonitor()")
    public Object proceed(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return execute(proceedingJoinPoint);
    }
}
