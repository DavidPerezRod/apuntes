package com.zerti.utilities.log.vo;

import com.zerti.utilities.log.repository.LogUtilities;
import com.zerti.utilities.log.vo.MethodExecutionStatus;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashMap;

@Setter
public class ExecutedMethodRepoInfo implements LogUtilities {

    private String className;
    private String methodName;
    private HashMap<String, Object> input;
    private Object output;
    private Long timeTookInMilliseconds;
    private MethodExecutionStatus status;
    private Timestamp timestamp;
}
