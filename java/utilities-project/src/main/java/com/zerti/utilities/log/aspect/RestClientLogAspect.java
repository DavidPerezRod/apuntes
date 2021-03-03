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
public class RestClientLogAspect extends LogAspect{

	@Autowired
	public RestClientLogAspect(
		RepositoryLogService logService, 
		@Qualifier("restClientLogAspectInfoCollector") LogInfoCollector logInfoCollector,
		ZertiMessageResolver zertiMessageResolver
	){
		super(logInfoCollector, logService, zertiMessageResolver);
	}

	@Around("execution(* com.zerti.utilities.rest.client.RestClientImpl.call(..))")
	public Object registerPSD2(ProceedingJoinPoint point) throws Throwable{
		return executeAndRegister(point);
	}
}
