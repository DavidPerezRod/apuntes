package com.zerti.utilities.log.aspect;

import com.zerti.utilities.log.vo.ExecutedMethodLogInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;


@Component
public interface LogInfoCollector {
    ExecutedMethodLogInfo createLogExecutedMethodInfo(final ProceedingJoinPoint proceedingJoinPoint);
    ExecutedMethodLogInfo completeLogSuccessExecutedMethodInfo(final Object methodOutput, final ExecutedMethodLogInfo executedMethodLogInfo);
    ExecutedMethodLogInfo completeLogErrorExecutedMethodInfo(final String errorMessage, final ExecutedMethodLogInfo executedMethodLogInfo);
}
