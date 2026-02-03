package com.zerti.utilities.log.aspect.collector;


import com.zerti.utilities.log.aspect.Kpi;
import com.zerti.utilities.log.vo.ExecutedMethodLogInfo;
import com.zerti.utilities.log.vo.KpiValues;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class KpiLogInfoCollector extends DefaultLogInfoCollector {

    @Override
    public ExecutedMethodLogInfo createLogExecutedMethodInfo(final ProceedingJoinPoint proceedingJoinPoint) {
        return getAdditionalInfo(proceedingJoinPoint, super.createLogExecutedMethodInfo(proceedingJoinPoint));
    }

    private ExecutedMethodLogInfo getAdditionalInfo(final ProceedingJoinPoint proceedingJoinPoint, final ExecutedMethodLogInfo executedMethodLogInfo){
        return executedMethodLogInfo.toBuilder().kPIValues(extractAnnotationValues(proceedingJoinPoint)).build();
    }

    private KpiValues extractAnnotationValues(final ProceedingJoinPoint proceedingJoinPoint) {
        Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
        Kpi kpi = method.getAnnotation(Kpi.class);
        return KpiValues.builder().service(kpi.service()).flow(kpi.flow()).domain(kpi.domain()).step(kpi.step()).dependency(kpi.dependency()).build();
    }
}
