package com.zerti.utilities.log.aspect;

import com.zerti.utilities.log.service.RepositoryLogService;
import com.zerti.utilities.message.ZertiMessageResolver;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerLogAspect extends LogAspect {

    @Autowired
    public ControllerLogAspect(
		RepositoryLogService logService, 
		@Qualifier("defaultLogInfoCollector") LogInfoCollector logInfoCollector,
		ZertiMessageResolver zertiMessageResolver
	) {
        super(logInfoCollector, logService, zertiMessageResolver);
    }


    //TODO todos los micros deben tener esta estructura para que el aspecto funcione. Posible mejora aspecto por anotación
    @Around("execution(* com.loycuspay.psd2.*.rest.*.*(..))")
    public Object registerLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return execute(proceedingJoinPoint);
    }

    //TODO todos los micros deben tener esta estructura para que el aspecto funcione. Posible mejora aspecto por anotación
    @Around("execution(* com.loycuspay.psd2.*.controller.*.*(..))")
    public Object registerControllerLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return execute(proceedingJoinPoint);
    }

}
