package com.zerti.utilities.log.service;

import com.zerti.utilities.log.vo.ExecutedMethodLogInfo;
import com.zerti.utilities.log.vo.ExecutedMethodRepoInfo;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LogToJSonObjectMapper{

    private LogToJSonObjectMapper(){}

    static ExecutedMethodRepoInfo logToRepoInfo(final ExecutedMethodLogInfo logInfo){
        ExecutedMethodRepoInfo repoInfo=new ExecutedMethodRepoInfo();
        repoInfo.setClassName(logInfo.getClassName());
        repoInfo.setMethodName(logInfo.getMethodName());
        repoInfo.setStatus(logInfo.getStatus());
        repoInfo.setTimestamp(Timestamp.valueOf(LocalDateTime.now(ZoneId.systemDefault()).plusHours(2L)));
        repoInfo.setTimeTookInMilliseconds(logInfo.getTimeTookInMilliseconds());
        repoInfo.setInput(logInfo.getInput());
        repoInfo.setOutput(logInfo.getOutput());
        return repoInfo;
    }
}
