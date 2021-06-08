package com.zerti.utilities.log.aspect.collector;

import org.apache.commons.httpclient.HttpMethodBase;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class RestClientLogAspectInfoCollector extends DefaultLogInfoCollector {

    @Override
    HashMap<String, Object> extractArgsInfo(final ProceedingJoinPoint proceedingJoinPoint) {
        String[] codeSignature = ((CodeSignature) proceedingJoinPoint.getSignature()).getParameterNames();
        HashMap<String, Object> logParams=new HashMap<>();
        Object[] args= proceedingJoinPoint.getArgs().clone();

        if (args!=null) {
            for (int i =0; i<args.length; i++){
                if (args[i] instanceof HttpMethodBase){
                    logParams.put("RequestHeaders", ((HttpMethodBase)args[i]).getRequestHeaders());
                }else{
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
