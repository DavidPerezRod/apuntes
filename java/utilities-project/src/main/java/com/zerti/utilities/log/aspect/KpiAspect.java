package com.zerti.utilities.log.aspect;

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
public class KpiAspect extends LogAspect {

    @Autowired
    KpiAspect(
		@Qualifier("kpiLogInfoCollector") LogInfoCollector logInfoCollector,
		ZertiMessageResolver zertiMessageResolver
	){
        super(logInfoCollector, zertiMessageResolver);
    }

    @Pointcut("@annotation(com.zerti.utilities.log.aspect.Kpi)")
    public void beanAnnotatedWithMonitor() {
    }

    @Pointcut("execution(public * *(..))")
    public void publicMethod() {
    }

    @Pointcut("publicMethod() && beanAnnotatedWithMonitor()")
    public void publicMethodInsideAClassMarkedWithAtMonitor() {
    }

    @Around("publicMethodInsideAClassMarkedWithAtMonitor()")
    public Object time(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return execute(proceedingJoinPoint);
    }
}
