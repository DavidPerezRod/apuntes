package com.zerti.utilities.log.aspect.collector;

import com.zerti.utilities.log.aspect.LogInfoCollector;
import com.zerti.utilities.log.vo.ExecutedMethodLogInfo;
import com.zerti.utilities.log.vo.MethodExecutionStatus;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;

import javax.servlet.ServletRequest;
import java.util.HashMap;

@Component
@Slf4j
public class DefaultLogInfoCollector implements LogInfoCollector {

    public ExecutedMethodLogInfo createLogExecutedMethodInfo(final ProceedingJoinPoint proceedingJoinPoint) {
        HashMap<String, Object> input=extractArgsInfo(proceedingJoinPoint);
        String className=proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName=proceedingJoinPoint.getSignature().getName();
        return ExecutedMethodLogInfo.builder().input(input).className(className).methodName(methodName).timestamp(System.currentTimeMillis()).status(MethodExecutionStatus.IN_PROGRESS_STATUS).build();
    }

    public ExecutedMethodLogInfo completeLogSuccessExecutedMethodInfo(final Object methodOutput, final ExecutedMethodLogInfo executedMethodLogInfo) {
        long timeTook=System.currentTimeMillis()-executedMethodLogInfo.getTimestamp();
        return executedMethodLogInfo.toBuilder().timeTookInMilliseconds(timeTook).output(methodOutput).status(MethodExecutionStatus.FINISHED_STATUS).build();
    }

    public ExecutedMethodLogInfo completeLogErrorExecutedMethodInfo(final String errorMessage, final ExecutedMethodLogInfo executedMethodLogInfo) {
        long timeTook=System.currentTimeMillis()-executedMethodLogInfo.getTimestamp();
        return executedMethodLogInfo.toBuilder().timeTookInMilliseconds(timeTook).output(errorMessage).indent(true).status(MethodExecutionStatus.ERROR_STATUS).build();
    }

    HashMap<String, Object> extractArgsInfo(final ProceedingJoinPoint proceedingJoinPoint) {
        String[] codeSignature = ((CodeSignature) proceedingJoinPoint.getSignature()).getParameterNames();
        HashMap<String, Object> logParams=new HashMap<>();
        Object[] args= proceedingJoinPoint.getArgs().clone();

        if (args!=null) {
            for (int i =0; i<args.length; i++){
                if (!(args[i] instanceof ServletRequest || args[i] instanceof RequestAttributes)){
                    if (args[i] != null)
                        logParams.put(codeSignature[i], args[i]);
                    else
                        logParams.put(codeSignature[i], "");
                }
            }
        }
        return logParams;
    }
}
