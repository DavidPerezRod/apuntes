package com.zerti.utilities.log.vo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zerti.utilities.message.ZertiMessageResolver;
import lombok.Builder;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import java.text.MessageFormat;
import java.util.HashMap;

import static com.zerti.utilities.message.MessageCodes.JSON_PARSING_ERROR;

@Getter
@Builder(toBuilder = true)
public class ExecutedMethodLogInfo {

    @JsonBackReference
    private HashMap<String, Object> input;

    private Object output;
    private String className;
    private String methodName;
    private Long timeTookInMilliseconds;
    private MethodExecutionStatus status;
    private Long timestamp;
    private ExceptionAspectInfo exceptionAspectInfo;
    private KpiValues kPIValues;

    @JsonIgnore
    private final boolean indent;

    public String toString(ZertiMessageResolver zertiMessageResolver) {
        String stringBody = "";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        if (indent)
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        try {
            stringBody = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            Logger logger = LoggerFactory.getLogger(ExecutedMethodLogInfo.class);
            String error = zertiMessageResolver.getMessage(JSON_PARSING_ERROR, LocaleContextHolder.getLocale());
            logger.error(MessageFormat.format(error, className));
        }
        return stringBody;
    }
}
