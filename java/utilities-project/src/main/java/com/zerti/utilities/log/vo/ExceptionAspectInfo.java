package com.zerti.utilities.log.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionAspectInfo {

    private final String cause;
    private final StackTraceElement[] stackTraceElements;
}
