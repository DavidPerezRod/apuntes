package me.dabiz.exception.info.type;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import me.dabiz.exception.info.ExceptionInfo;
import me.dabiz.exception.info.category.base.ComposedCategoryInfo;
import me.dabiz.exception.info.category.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public  class UncheckedException extends RuntimeException implements ZertiException {

    protected final ExceptionInfo info;

    public UncheckedException(final ExceptionInfo info) {
        this.info = info;
    }

    public UncheckedException(final Throwable cause, final ExceptionInfo info) {
        super(cause);
        this.info = info;
    }

    public void addZertiExceptionBodyLeaf(final Category exceptionBodyLeaf){
        info.getInfo().addCategoryInfo(exceptionBodyLeaf);
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
            Logger logger = LoggerFactory.getLogger(ComposedCategoryInfo.class);
//            String error = zertiMessageResolver.getMessage(JSON_PARSING_ERROR, LocaleContextHolder.getLocale());
//            logger.error(error);
        }
        return stringBody;
    }
}
