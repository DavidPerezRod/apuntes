package com.zerti.utilities.log.aspect;

import com.zerti.utilities.log.service.RepositoryLogService;
import com.zerti.utilities.log.vo.ExceptionAspectInfo;
import com.zerti.utilities.log.vo.ExecutedMethodLogInfo;
import com.zerti.utilities.message.ZertiMessageResolver;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Arrays;

import static com.zerti.utilities.message.MessageCodes.ASPECT_PROCEED_ERROR_LOG;

public abstract class LogAspect {
	
    private final LogInfoCollector logInfoCollector;
    private RepositoryLogService logService;
    private final ZertiMessageResolver zertiMessageResolver;
    
    LogAspect(
		final LogInfoCollector logInfoCollector, 
		final RepositoryLogService logService,
		final ZertiMessageResolver zertiMessageResolver
	) {
        this.logInfoCollector = logInfoCollector;
        this.logService = logService;
        this.zertiMessageResolver=zertiMessageResolver;
    }

    LogAspect(
		final LogInfoCollector logInfoCollector,
		final ZertiMessageResolver zertiMessageResolver
	) {
        this.logInfoCollector = logInfoCollector;
        this.zertiMessageResolver=zertiMessageResolver;
    }

    public Object execute(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        return this.invokeMethod(proceedingJoinPoint).getOutput();
    }

    public Object executeAndRegister(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        ExecutedMethodLogInfo executedMethodLogInfo=invokeMethod(proceedingJoinPoint);
        logService.register(executedMethodLogInfo);
        return executedMethodLogInfo.getOutput();
    }

    private ExecutedMethodLogInfo invokeMethod(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Logger logger = LoggerFactory.getLogger(proceedingJoinPoint.getTarget().getClass());
        ExecutedMethodLogInfo methodLogInfo = logInfoCollector.createLogExecutedMethodInfo(proceedingJoinPoint);
        Object value;
        try {
            value = proceedingJoinPoint.proceed();
        } catch (Throwable cause) {
            logger.error(extractExceptionInfo(cause, methodLogInfo));
            throw cause;
        }
        methodLogInfo=logInfoCollector.completeLogSuccessExecutedMethodInfo(value, methodLogInfo);
        logger.info(methodLogInfo.toString(zertiMessageResolver));
        return methodLogInfo;
    }

    private String extractExceptionInfo(Throwable cause, ExecutedMethodLogInfo methodLogInfo) {
        String[] arguments = {methodLogInfo.getMethodName(), methodLogInfo.getClassName()};
        String message = zertiMessageResolver.getMessage(ASPECT_PROCEED_ERROR_LOG, arguments, LocaleContextHolder.getLocale());

        StackTraceElement[] elements = filterBusinessStackElements(cause);
        ExceptionAspectInfo exceptionAspectInfo = ExceptionAspectInfo.builder().cause(cause.getLocalizedMessage()).stackTraceElements(elements).build();

        return logInfoCollector.completeLogErrorExecutedMethodInfo(message, methodLogInfo.toBuilder().exceptionAspectInfo(exceptionAspectInfo).build()).toString();
    }

    private StackTraceElement[] filterBusinessStackElements(Throwable cause) {
        return Arrays.stream(cause.getStackTrace()).filter(stackTraceElement -> {
            String classLoaderName = stackTraceElement.getClassName().toLowerCase();
//TODO los nombres deben estar parametrizados
            return (classLoaderName.contains("loycuspay") ||
                    classLoaderName.contains("zerti"));
        }).toArray(StackTraceElement[]::new);
    }
}
