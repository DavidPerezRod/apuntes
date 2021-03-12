package me.dabiz.exception.type;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import me.dabiz.exception.info.ExceptionInfo;
import me.dabiz.exception.info.body.base.CategoryInfoComposedBody;
import me.dabiz.exception.info.body.subcategory.SubcategoryInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public  class ZertiUncheckedException extends RuntimeException implements ZertiException {

    protected final ExceptionInfo info;

    public ZertiUncheckedException(final ExceptionInfo info) {
        this.info = info;
    }

    public ZertiUncheckedException(final Throwable cause, final ExceptionInfo info) {
        super(cause);
        this.info = info;
    }

    public void addZertiExceptionBodyLeaf(final SubcategoryInfo exceptionBodyLeaf){
        info.getBody().addExceptionBodyLeaf(exceptionBodyLeaf);
    }

    @Override
    public ExceptionInfo getExceptionInfo() {
        return info;
    }

    @Override
    public String toString() {
        String stringBody = "";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        try {
            stringBody = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            Logger logger = LoggerFactory.getLogger(CategoryInfoComposedBody.class);
//            String error = zertiMessageResolver.getMessage(JSON_PARSING_ERROR, LocaleContextHolder.getLocale());
//            logger.error(error);
        }
        return stringBody;
    }
}
